package com.tekzee.amiggos.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.prongbang.sbs.SmartBottomSheetDialogFragment
import com.tekzee.amiggos.R
import kotlinx.android.synthetic.main.custom_layout_pick_photo.*

class PickPhotoCustomDialog(
	supportFragmentManager: FragmentManager
) : SmartBottomSheetDialogFragment(supportFragmentManager) {

	private var onClickCloseListener: (() -> Unit)? = null
	private var onClickPickExistingPhoto: (() -> Unit)? = null
	private var onClickTakeNewPhoto: (() -> Unit)? = null

	override fun tagName(): String = PickPhotoCustomDialog::class.java.simpleName

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.custom_layout_pick_photo, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initView()
	}

	private fun initView() {
		arguments?.let { args ->
			uploadexistingphoto.text = args.getString(UPLOADEXISTINGPHOTO)
			takenewphoto.text = args.getString(TAKENEWPHOTO)
			cancel.text = args.getString(CANCEL)
			cancel.setOnClickListener {
				dismiss()
				onClickCloseListener?.invoke()
			}
			uploadexistingphoto.setOnClickListener{
				dismiss()
				onClickPickExistingPhoto!!.invoke()
			}
			takenewphoto.setOnClickListener{
				dismiss()
				onClickTakeNewPhoto!!.invoke()
			}
		}
	}

	class Builder(
		private val fragmentManager: FragmentManager
	) : SmartBottomSheetDialogFragment.Builder<PickPhotoCustomDialog>() {

		private var onCloseListener: (() -> Unit)? = null
		private var onPickExistingPhotoListener: (() -> Unit)? = null
		private var onPickTakeNewPhotoListener: (() -> Unit)? = null

		override fun build(): PickPhotoCustomDialog {
			return PickPhotoCustomDialog(fragmentManager)
				.apply {
					arguments = argumentsBundle.apply {
						onClickCloseListener = onCloseListener
						onClickPickExistingPhoto = onPickExistingPhotoListener
						onClickTakeNewPhoto = onPickTakeNewPhotoListener
					}
				}
		}

		fun setUploadExistingPhotoText(title: String): Builder {
			argumentsBundle.putString(UPLOADEXISTINGPHOTO, title)
			return this
		}

		fun setTakeNewPhotoText(subtitle: String): Builder {
			argumentsBundle.putString(TAKENEWPHOTO, subtitle)
			return this
		}

		fun setCancelText(subtitle: String): Builder {
			argumentsBundle.putString(CANCEL, subtitle)
			return this
		}

		fun setOnCloseButton(onListener: (() -> Unit)? = null): Builder {
			onCloseListener = onListener
			return this
		}

		fun setOnTakeNewPhoto(onListener: (() -> Unit)? = null): Builder {
			onPickTakeNewPhotoListener = onListener
			return this
		}
		fun setOnUploadExistingPhoto(onListener: (() -> Unit)? = null): Builder {
			onPickExistingPhotoListener = onListener
			return this
		}
	}

	companion object {
		private const val UPLOADEXISTINGPHOTO = "UPLOADEXISTINGPHOTO"
		private const val TAKENEWPHOTO = "TAKENEWPHOTO"
		private const val CANCEL = "CANCEL"
	}
}