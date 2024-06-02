package com.example.deliverytracker.presentation.orderDetails

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.deliverytracker.R
import com.example.deliverytracker.databinding.ActivityPhotoBinding
import com.example.deliverytracker.presentation.orderList.OrdersFragmentDirections
import com.example.deliverytracker.utils.CameraFailureListener
import com.example.deliverytracker.utils.Constants
import com.example.deliverytracker.utils.FunctionUtils
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PhotoActivity : AppCompatActivity(), CameraFailureListener {
    private var binding: ActivityPhotoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.faceDetectionCameraView?.run {
            facing = Facing.BACK
            setFailureListener(this@PhotoActivity)
            setLifecycleOwner(this@PhotoActivity)
        }

        binding?.btnSubmit?.setOnClickListener {
            binding?.faceDetectionCameraView?.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(jpeg: PictureResult) {
                    super.onPictureTaken(jpeg)
                    try {
                        binding?.faceDetectionCameraView?.close()

                        val options = BitmapFactory.Options()
                        options.inJustDecodeBounds = true

                        BitmapFactory.decodeByteArray(jpeg.data, 0, jpeg.data.size, options)

                        var actualHeight = options.outHeight.toFloat()
                        var actualWidth = options.outWidth.toFloat()

                        // setting inSampleSize value allows to load a scaled down version of the original image
                        options.inSampleSize =
                            FunctionUtils.calculateInSampleSize(
                                options,
                                actualWidth.toInt(),
                                actualHeight.toInt()
                            )

                        // max Height and width values of the compressed image is taken as
                        val maxHeight = 1024.0f
                        val maxWidth = 1024.0f
                        var imgRatio = (actualWidth / actualHeight)

                        val maxRatio = maxWidth / maxHeight

                        // width and height values are set maintaining the aspect ratio of the image
                        if (actualHeight > maxHeight || actualWidth > maxWidth) {
                            when {
                                imgRatio < maxRatio -> {
                                    imgRatio = maxHeight / actualHeight
                                    actualWidth = (imgRatio * actualWidth)
                                    actualHeight = maxHeight
                                }
                                imgRatio > maxRatio -> {
                                    imgRatio = (maxWidth / actualWidth)
                                    actualHeight = (imgRatio * actualHeight)
                                    actualWidth = maxWidth
                                }
                                else -> {
                                    actualHeight = maxHeight
                                    actualWidth = maxWidth
                                }
                            }
                        }

                        //inJustDecodeBounds set to false to load the actual bitmap
                        options.inJustDecodeBounds = false

                        var bmp: Bitmap? = null
                        var scaledBitmap: Bitmap? = null
                        try {
                            bmp =
                                BitmapFactory.decodeByteArray(jpeg.data, 0, jpeg.data.size, options)
                            scaledBitmap =
                                Bitmap.createBitmap(
                                    actualWidth.toInt(),
                                    actualHeight.toInt(),
                                    Bitmap.Config.ARGB_8888
                                )
                        } catch (e: OutOfMemoryError) {
                            Log.d("", "OutOfMemoryError", e)
                        }

                        val ratioX = actualWidth / options.outWidth
                        val ratioY = actualHeight / options.outHeight
                        val middleX = actualWidth / 2.0f
                        val middleY = actualHeight / 2.0f

                        val scaleMatrix = Matrix()
                        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

                        val canvas = Canvas(scaledBitmap!!)
                        canvas.setMatrix(scaleMatrix)
                        canvas.drawBitmap(
                            bmp!!,
                            middleX - bmp.width / 2f,//
                            middleY - bmp.height / 2f,//
                            Paint(Paint.FILTER_BITMAP_FLAG)
                        )

                        bmp.recycle()

                        val dstBmp: Bitmap
                        val matrix = Matrix()
                        if (scaledBitmap.width >= scaledBitmap.height) {
                            matrix.postRotate(270f)
                            dstBmp = Bitmap.createBitmap(
                                scaledBitmap,
                                0,//scaledBitmap.width / 2 - scaledBitmap.height / 2
                                0,
                                scaledBitmap.width,
                                scaledBitmap.height, matrix, true
                            )
                        } else {
                            dstBmp = Bitmap.createBitmap(
                                scaledBitmap,
                                0,
                                0,//scaledBitmap.height / 2 - scaledBitmap.width / 2
                                scaledBitmap.width,
                                scaledBitmap.height,
                                matrix,
                                false
                            )
                        }
                        scaledBitmap.recycle()

                        lifecycleScope.launch(Dispatchers.Main) {
                            val pictureFile = saveBitmap(this@PhotoActivity, dstBmp)
                            Log.d("PFilePath","${pictureFile?.path}")
                            setResult(Constants.RESULT_IMAGE_CAPTURED, Intent().apply { putExtra(Constants.IMAGE_FILE_PATH,pictureFile?.path) })
                            finish()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@PhotoActivity,"Exception occurred",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCameraError(exception: CameraException) {
//                    if (requireActivity() is KYCDocumentsNewActivity) {
//                        (requireActivity() as KYCDocumentsNewActivity).removeFragment(
//                            arguments?.getInt(
//                                "type",
//                                -1
//                            ) ?: 3
//                        )
//                    }
//                    Util.reportCrashlytics(exception)
                    Toast.makeText(this@PhotoActivity,"Error occurred",Toast.LENGTH_SHORT).show()
                }
            })
            binding?.faceDetectionCameraView?.takePicture()
        }
    }
    suspend fun saveBitmap(context: Context, image: Bitmap): File? =
        withContext(Dispatchers.IO) {
            val pictureFileDir = File(FunctionUtils.getFilePath(context, null), "images")
            if (!pictureFileDir.exists()) {
                val isDirectoryCreated = pictureFileDir.mkdirs()
                if (!isDirectoryCreated) {
                    return@withContext null
                }
            }
            val filename =
                pictureFileDir.path + File.separator + System.currentTimeMillis() + ".jpg"
            val pictureFile = File(filename)
            try {
                pictureFile.createNewFile()
                val oStream = FileOutputStream(pictureFile)
                image.compress(Bitmap.CompressFormat.JPEG, 50, oStream)
                oStream.flush()
                oStream.close()
            } catch (e: Exception) {

            }
            pictureFile
        }

    override fun onCameraFailure() {
        Toast.makeText(this@PhotoActivity,"Camera hardware issue",Toast.LENGTH_SHORT).show()
    }
}