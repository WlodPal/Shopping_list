package com.vladimir.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {


    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button


    private var screenMode: String = MODE_UNKNOWN
    private var shopItemID: Int = ShopItem.UNDEFINED_ID

    // fragment может тоже работать с ViewModel
    private lateinit var viewModel: ShopItemViewModel

    /*
    pars params нужно вызывать сдесь но не обезательно
    это для того чтобы если каке-то параметры не переданы мы не создавали View
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        purseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    // момент в который мы точно заем что View создана
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        chooseWrightMode()
        observeFromViewModel()
    }


    private fun observeFromViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }

        viewModel.closeScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
            /*
            requireActivity().onBackPressed() -> не нулабельное
            но если вернется null то програма упадет
            activity?.onBackPressed() -> нулабельный
             */
        }
    }

    private fun chooseWrightMode() {
        when (screenMode) {
            MODE_EDIT -> lunchEditMode()
            MODE_ADD -> lunchAddMode()
        }
    }

    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun lunchEditMode() {
        viewModel.getShopItem(shopItemID)
        /* во viewModel надо передавать viewLifecycleOwner а не this
        раньше можна было передавть this так как у фрагментов тоже есть
        свой жизненый цикл. Обычно внутри observer мы работаем с view элементами
        и если ее не существует то необходимо отписаться от LiveData
        благодоря viewLifecycleOwner это приисходить автоматически.
        может быть что view уже умерла а фрамгмент еще жив поетому this не используем.
        viewLifecycleOwner - это жизненый цикл созданой view а не фрагмента
        */
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun lunchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    // проверяем все ми методы переданы успешно
    private fun purseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemID = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }


//        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
//            throw RuntimeException("Param screen mode is absent")
//        }
//        if (screenMode == MODE_EDIT && shopItemID == ShopItem.UNDEFINED_ID) {
//            throw RuntimeException("Param shop item id is absent")
//        }

        /* чтобы показать фрагмент intent не используется
        // requireActivity() возвращет ссылку на активити к которой прикреплен данный fragment
         фрагмент не может требовать от activity чтобы она были запущена каким-то определеном образом
         фрагмент самостоятельный элемент и у него есть своя ViewModel и он может требовать какие-то
         параметры при запуске. Но если он требует чтобы Activity содержала какието параметры
         то это не правильно архетектурно плюс мы не сможем использовать этот фрагмент
         в другой Activity без параметров
            if (!requireActivity().intent.hasExtra(EXTRA_SCREEN_MODE)) {
                throw RuntimeException("Param screen mode is absent")
           }
           */
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.b_save)
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
        private const val SHOP_ITEM_ID = "extra_shop_item_id"


        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }

            // правильно но не в стиле котлин это на Java
//            val args = Bundle()
//            args.getString(SCREEN_MODE, MODE_ADD)
//            val fragment = ShopItemFragment()
//            fragment.arguments = args
//            return fragment
        }

        fun newInstanceEditItem(shopItemID: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemID)
                }
            }
        }
    }
}