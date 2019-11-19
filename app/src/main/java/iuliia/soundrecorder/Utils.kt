package iuliia.soundrecorder

import android.content.Context
import java.io.File

fun getDirectory(context: Context): File {
    return context.externalCacheDir!!
}