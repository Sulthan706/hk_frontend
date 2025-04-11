package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemNotificationBinding
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.service.NotifVendorService
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.DataArrayContent
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.detailProcessComplaint.DetailProcessComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel.NotifVendorViewModel
import com.hkapps.hygienekleen.retrofit.AppRetrofit
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class NotifVendorMidLevelAdapter(
    private var context: Context? = null,
    private val lifecycleOwner: LifecycleOwner,
    private var viewModel: NotifVendorViewModel,
//    var notificationsContent: ArrayList<DataMidArrayContent>
    var notificationsContent: ArrayList<DataArrayContent>
) :
    RecyclerView.Adapter<NotifVendorMidLevelAdapter.ViewHolder>() {
    private lateinit var notificationCallback: NotificationCallback
    private lateinit var mService: NotifVendorService

    val detailModel = MutableLiveData<DetailProcessComplaintResponse>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = notificationsContent[position]
        val id = response.complaintId

//        viewModel.getDetailProcessComplaint(id)
//        setObserver(holder, id, position)


        holder.binding.tvStatusComplaintNotif.text = "Load data"

        holder.binding.tvTitleNotif.text = "Load data"
        holder.binding.tvClientNameNotif.text = "Load data"
        holder.binding.tvDescriptionNotif.text = "Load data"

        holder.binding.tvDateNotif.text = "Load data"
        holder.binding.tvTimeNotif.text = "Load data"

        holder.binding.tvLocationNotif.text = "Load data"
        holder.binding.tvSubLocationNotif.text = "Load data"
        holder.binding.tvStatusComplaintNotif.text = "Load data"

        mService = AppRetrofit.notifVendorService
        mService.getDetailProcessComplaint(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<DetailProcessComplaintResponse>() {
                override fun onSuccess(t: DetailProcessComplaintResponse) {
                    detailModel.value = t
                    holder.binding.tvTitleNotif.text = t.data.title
                    holder.binding.tvClientNameNotif.text = t.data.clientName
                    if (t.data.statusComplaint == "WAITING"){
                        holder.binding.tvDateNotif.text = t.data.date
                        holder.binding.tvTimeNotif.text = t.data.time
                        holder.binding.tvDescriptionNotif.text = t.data.description
                    }else if (t.data.statusComplaint == "ON PROGRESS"){
                        holder.binding.tvDateNotif.text = t.data.date
                        holder.binding.tvTimeNotif.text = t.data.time
                        holder.binding.tvDescriptionNotif.text = t.data.description
                    }else if (t.data.statusComplaint == "DONE"){
                        if (t.data.doneAtDate != null){
                            holder.binding.tvDateNotif.text = "" + t.data.doneAtDate
                        }else{
                            holder.binding.tvDateNotif.text= "-"
                        }
                        holder.binding.tvTimeNotif.text = ""
                        holder.binding.tvDescriptionNotif.text = t.data.description
                    }else if (t.data.statusComplaint == "CLOSE"){
                        if (t.data.doneAtDate != null){
                            holder.binding.tvDateNotif.text = "" + t.data.doneAtDate
                        }else{
                            holder.binding.tvDateNotif.text= "-"
                        }
                        holder.binding.tvTimeNotif.text = ""
                        holder.binding.tvDescriptionNotif.text = t.data.description
                    }


                    holder.binding.tvLocationNotif.text = t.data.locationName
                    holder.binding.tvSubLocationNotif.text = t.data.locationName
                    holder.binding.tvStatusComplaintNotif.text = t.data.statusComplaint

                    val url =
                        context!!.getString(R.string.url) + "assets.admin_master/images/complaint/"
                    context.let {
                        it?.let { it1 ->
                            val requestOptions = RequestOptions()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                                .skipMemoryCache(true)
                            Glide.with(it1)
                                .load(url + t.data.image)
                                .apply(requestOptions)
                                .into(holder.binding.ivComplaintNotif)

                        }
                    }

//                     set photo profile
                    val urls = context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/"
                    context.let {
                        it?.let { it1 ->
                            if (t.data.clientPhotoProfile == "null" || t.data.clientPhotoProfile == null || t.data.clientPhotoProfile == "") {
                                val uri =
                                    "@drawable/profile_default" // where myresource (without the extension) is the file
                                val imageResource =
                                    it.resources.getIdentifier(uri, null, it.packageName)
                                val res = it.getDrawable(imageResource)
                                holder.binding.ivClientNotif.setImageDrawable(res)
                            } else {
                                val requestOptions = RequestOptions()
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                                    .skipMemoryCache(true)
                                Glide.with(it1)
                                    .load(urls + t.data.clientPhotoProfile)
                                    .apply(requestOptions)
                                    .into(holder.binding.ivClientNotif)
                            }
                        }
                    }

                    when (t.data.statusComplaint) {
                        "WAITING" -> {
                            holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                            holder.binding.tvStatusComplaintNotif.text = "Mengunggu"
                        }
                        "ON PROGRESS" -> {
                            holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                            holder.binding.tvStatusComplaintNotif.text = "Dikerjakan"
                        }
                        "DONE" -> {
                            holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_green)
                            holder.binding.tvStatusComplaintNotif.text = "Selesai"
                        }
                        "CLOSE" -> {
                            holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_disable)
                            holder.binding.tvStatusComplaintNotif.text = "Tutup"
                        }
                        else -> {
                            holder.binding.rlStatusComplaintNotif.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                            holder.binding.tvStatusComplaintNotif.text = "Error"
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        holder.binding.tvStatusComplaintNotif.text = "Unknown"

                        holder.binding.tvTitleNotif.text = "Unknown"
                        holder.binding.tvClientNameNotif.text = "Unknown"
                        holder.binding.tvDescriptionNotif.text = "Unknown"

                        holder.binding.tvDateNotif.text = "Unknown"
                        holder.binding.tvTimeNotif.text = "Unknown"

                        holder.binding.tvLocationNotif.text = "Unknown"
                        holder.binding.tvSubLocationNotif.text = "Unknown"
                        holder.binding.tvStatusComplaintNotif.text = "Unknown"

                        val errorBody = e.response()?.errorBody()
                        val gson = Gson()
                        val error = gson.fromJson(
                            errorBody?.string(),
                            DetailProcessComplaintResponse::class.java
                        )
                        detailModel.value = error
                    } else {
                        Log.e("NorifVendorVM", "onError: can't get detail complaint")
                    }
                }

            })
    }


    fun setListener(notificationCallback: NotificationCallback) {
        this.notificationCallback = notificationCallback
    }

    interface NotificationCallback {
        fun onClickNotification(
            notificationId: Int,
            position: Int,
        )
    }

    //item not change when scroll
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //item not change when scroll
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return notificationsContent.size
    }

    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var position = adapterPosition
            var notification = notificationsContent[position]
            notificationCallback.onClickNotification(
                notification.complaintId,
                position
            )
        }
    }

}