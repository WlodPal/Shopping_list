package com.vladimir.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.vladimir.shoppinglist.R
import com.vladimir.shoppinglist.databinding.FragmentShopItemBinding
import com.vladimir.shoppinglist.domain.ShopItem
import javax.inject.Inject

class ShopItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as ShopListApp).component
    }

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemID: Int = ShopItem.UNDEFINED_ID


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }
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
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    // момент в который мы точно заем что View создана
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        chooseWrightMode()
        observeFromViewModel()
    }


    private fun observeFromViewModel() {

        viewModel.closeScreen.observe(viewLifecycleOwner) {

            onEditingFinishedListener.onEditingFinished()

//            activity?.onBackPressed()


            // такое решение плохое когда явно приводим тип
            //(activity as MainActivity).onEditingFinished()

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
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.etCount.addTextChangedListener(object : TextWatcher {
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
        binding.bSave.setOnClickListener {
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    private fun lunchAddMode() {
        binding.bSave.setOnClickListener {
            viewModel.addShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
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