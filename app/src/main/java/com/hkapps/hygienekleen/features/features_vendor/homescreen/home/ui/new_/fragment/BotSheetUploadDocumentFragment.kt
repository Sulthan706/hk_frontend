package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.FragmentBottomSheetktpBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.OpenDocumentUploadActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.UploadDocumentPhotoActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetUploadDocumentFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetktpBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    var checkDocumentktp =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.CHECK_DOCUMENT_KTP,false)
    var checkDocumentijazah =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.CHECK_DOCUMENT_IJAZAH,false)
    var checkDocumentsertifikat =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.CHECK_DOCUMENT_SERTIFIKAT_KOMPETENSI,false)
    var checkDocumentsio =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.CHECK_DOCUMENT_SIO,false)
    var checkDocumentskck =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.CHECK_DOCUMENT_SKCK,false)

    var imageKTP =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_KTP, "")
    var imageIjazah =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_IJAZAH,"")
    var imageSertifikat =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_SERTIFIKAT,"")
    var imageSIO =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_SIO,"")
    var imageSkck =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_SKCK,"")

    var typeDocument =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_DOCUMENT, "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetktpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlTakePhotoBottomDocument.setOnClickListener {
            startActivity(Intent(requireActivity(), UploadDocumentPhotoActivity::class.java))
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, typeDocument)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "camera")
            dismiss()
        }
        binding.rlTakePhotoFromFolderDocument.setOnClickListener {
            startActivity(Intent(requireActivity(), UploadDocumentPhotoActivity::class.java))
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "gallery")
            dismiss()
        }

        when(typeDocument){
            "KTP" -> {
                if (!checkDocumentktp){
                    binding.rlOpenPhotoBottomDocument.visibility = View.GONE
                } else {
                    binding.rlOpenPhotoBottomDocument.visibility = View.VISIBLE
                    binding.rlOpenPhotoBottomDocument.setOnClickListener {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, typeDocument)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.IMAGE_KTP, imageKTP)
                        startActivity(Intent(requireActivity(), OpenDocumentUploadActivity::class.java))
                        dismiss()
                    }
                }
            }
            "IJAZAH" -> {
                if (!checkDocumentijazah){
                    binding.rlOpenPhotoBottomDocument.visibility = View.GONE
                } else {
                    binding.rlOpenPhotoBottomDocument.visibility = View.VISIBLE
                    binding.rlOpenPhotoBottomDocument.setOnClickListener {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, typeDocument)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.IMAGE_IJAZAH, imageIjazah)
                        startActivity(Intent(requireActivity(), OpenDocumentUploadActivity::class.java))
                        dismiss()
                    }
                }
            }
            "SERTIFIKAT KOMPETENSI" ->{
                if (!checkDocumentsertifikat){
                    binding.rlOpenPhotoBottomDocument.visibility = View.GONE
                } else {
                    binding.rlOpenPhotoBottomDocument.visibility = View.VISIBLE
                    binding.rlOpenPhotoBottomDocument.setOnClickListener {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, typeDocument)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.IMAGE_SERTIFIKAT, imageSertifikat)
                        startActivity(Intent(requireActivity(), OpenDocumentUploadActivity::class.java))
                        dismiss()
                    }
                }
            }
            "SIO" -> {
                if (!checkDocumentsio){
                    binding.rlOpenPhotoBottomDocument.visibility = View.GONE
                } else {
                    binding.rlOpenPhotoBottomDocument.visibility = View.VISIBLE
                    binding.rlOpenPhotoBottomDocument.setOnClickListener {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, typeDocument)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.IMAGE_SIO, imageSIO)
                        startActivity(Intent(requireActivity(), OpenDocumentUploadActivity::class.java))
                        dismiss()
                    }
                }
            }
            "SKCK" -> {
                if (!checkDocumentskck){
                    binding.rlOpenPhotoBottomDocument.visibility = View.GONE
                } else {
                    binding.rlOpenPhotoBottomDocument.visibility = View.VISIBLE
                    binding.rlOpenPhotoBottomDocument.setOnClickListener {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, typeDocument)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.IMAGE_SKCK, imageSkck)
                        startActivity(Intent(requireActivity(), OpenDocumentUploadActivity::class.java))
                        dismiss()
                    }
                }
            }
        }





    }



}