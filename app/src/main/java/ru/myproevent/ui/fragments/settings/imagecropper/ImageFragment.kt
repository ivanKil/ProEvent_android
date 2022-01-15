package ru.myproevent.ui.fragments.settings.imagecropper

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import ru.myproevent.R
import ru.myproevent.databinding.FragmentUserImageBinding

class ImageFragment : Fragment() {

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .getIntent(requireActivity())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }
    private var _binding: FragmentUserImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var image: Drawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = requireContext().resources.getDrawable(
            R.drawable.ic_profile_picture_placeholder_big,
            requireContext().theme
        )
        binding.userImage.setImageDrawable(image)
        initViews()
    }

    private fun initViews() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri -> binding.userImage.setImageURI(uri) }
        }
        binding.imageLoad.setOnClickListener { cropActivityResultLauncher.launch(null) }
        binding.imageEdit.setOnClickListener { cropActivityResultLauncher.launch(null) }
    }

    companion object {
        fun newInstance() = ImageFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
