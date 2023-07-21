package av.imageview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiBlob;
import org.appcelerator.titanium.util.TiConvert;

@Kroll.module(name="AvImageviewNew", id="av.imageview")
public class ImageViewModule extends KrollModule
{
	private static final String LCAT = "ImageViewModule";
	private static final boolean DBG = TiConfig.LOGD;
	private Bitmap theBitmap = null;

	@Kroll.constant
	public static final String CONTENT_MODE_ASPECT_FILL = ImageViewConstants.CONTENT_MODE_ASPECT_FILL;

	@Kroll.constant
	public static final String CONTENT_MODE_ASPECT_FIT = ImageViewConstants.CONTENT_MODE_ASPECT_FIT;

	public ImageViewModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{

	}

	@SuppressLint("StaticFieldLeak")
	@Kroll.method
	public void resizeImage(KrollDict options) {

		TiBlob blob = TiConvert.toBlob(options.get("blob"));
		int width = options.getInt("width");
		int height = options.getInt("height");

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				Looper.prepare();
				try {
					theBitmap = Glide.with(TiApplication.getAppCurrentActivity())
							.asBitmap()
							.load(blob.getImage())
							.override(width, height)
							.centerCrop()
							.submit()
							.get();
				} catch (final Exception e) {
					Log.e(LCAT, e.getMessage());
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void dummy) {
				if (null != theBitmap) {
					KrollDict kd = new KrollDict();
					TiBlob blob = TiBlob.blobFromImage(theBitmap);
					kd.put("blob", blob);
					fireEvent("complete", kd);
				};
			}
		}.execute();
	}
}

