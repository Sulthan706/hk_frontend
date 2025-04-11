import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotUpdateNumbBpjsBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.bpjs.BotUploadBpjsDocumentFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotUpdateNumbBpjsFragment(val tittle : String? = null) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotUpdateNumbBpjsBinding
    private var number: String = ""
    private var typeBpjs =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_BPJS, "")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBotUpdateNumbBpjsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCloseBottomBpjs.setOnClickListener {
            dismiss()
        }

        if(tittle != null){
            binding.tvTitleUpdateNumbBpjs.text = tittle
        }

        val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.primary_color)
        binding.etBotNumbBpjs.setBoxStrokeColorStateList(colorStateList!!)
        binding.etBotNumbBpjs.hintTextColor = colorStateList

        binding.tvBotNumbBpjs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etBotNumbBpjs.helperText = null

            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()

                if (inputText.isEmpty()) {
                    binding.btnUploadDocumentBpjs.visibility = View.GONE
                } else if (inputText.length == 11) {
                    binding.btnUploadDocumentBpjs.visibility = View.VISIBLE
                } else {
                    binding.btnUploadDocumentBpjs.visibility = View.GONE
                }
                number = s.toString()


            }

        })


        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding.btnUploadDocumentBpjs.setOnClickListener {
            if (typeBpjs == "BPJSKES"){
                CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJS, number)
            } else {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_JAMSOSTEK, number)
            }
            BotUploadBpjsDocumentFragment().show(requireFragmentManager(),"botsheetuploaddocument")
            dismiss()
        }
        binding.btnUploadDocumentBpjsEmtpy.setOnClickListener {
            val currentHelperText = binding.etBotNumbBpjs.helperText?.toString()

            if (currentHelperText.isNullOrEmpty()) {
                // Show the helper text
                binding.etBotNumbBpjs.helperText = "Masukan 11 digit angka"
            }
        }

    }



}
