package sibsutis.boris.lab4;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity{

    SensorManager m; // переменная для работы с сенсорами
    Panel dr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = (SensorManager)getSystemService(SENSOR_SERVICE);
        dr = new Panel(this);
        setContentView(dr);
    }

    @Override
    protected void onStart() {
        super.onStart();
        m.registerListener(listenerLight, m.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_FASTEST);
        m.registerListener(listenerOrientation,m.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
        m.registerListener(listenerGravity,m.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_FASTEST);
        m.registerListener(listenerMagnetic,m.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_FASTEST);
        m.registerListener(listenerPressure,m.getDefaultSensor(Sensor.TYPE_PRESSURE),SensorManager.SENSOR_DELAY_FASTEST);
    }

    SensorEventListener listenerPressure = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            dr.d = event.values[0];
            dr.invalidate();
        }
    };
    SensorEventListener listenerMagnetic = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            dr.m = event.values[0];
            dr.invalidate();
        }
    };
    SensorEventListener listenerGravity = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            dr.g = event.values[0];
            dr.invalidate();
        }
    };
    SensorEventListener listenerOrientation = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            dr.p = event.values[0];
            dr.invalidate();
        }
    };
    SensorEventListener listenerLight = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            dr.sv = event.values[0];
            dr.invalidate();
        }
    };

    @Override
    protected void onDestroy(){
        m.unregisterListener(listenerLight);
        m.unregisterListener(listenerOrientation);
        m.unregisterListener(listenerGravity);
        m.unregisterListener(listenerMagnetic);
        m.unregisterListener(listenerPressure);
    }

    class Panel extends View {
        float p = 0; // угол к северу
        float sv = 0; // до 30000 освещение
        float g = 0; // гравитация м/с^2
        float m = 0; // магнитное поле
        float d = 0; // давление hPa
        public Panel(Context context){
            super(context);
        }
        public void onDraw(Canvas canvas){
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.kompas);
            Matrix matrix=new Matrix();
            matrix.preRotate((-1)*p,b.getWidth()/2, b.getHeight()/2);
            matrix.postTranslate(canvas.getHeight()/2 - b.getWidth(), canvas.getWidth()/2);
            canvas.drawBitmap(b,matrix,null);

            Paint p = new Paint();
            p.setARGB((int) sv, 255, 255, 0);
            canvas.drawCircle(50, 50, 50, p);

            Paint p1 = new Paint();
            p1.setColor(Color.BLACK);
            p1.setTextSize(30);
            canvas.drawText("Ускорение свободного падения: " + String.format("%.3f",g) + " м/с^2", 100, 30, p1);
            canvas.drawText("Магнитное поле: " + String.format("%.2f",m) + " мТл",100,60,p1);
            canvas.drawText("Давление: " + String.format("%.2f",d) + " гПа == " + String.format("%.2f",d * 100 * 0.0075006375541921) + " мм рт.ст",100,90,p1);

            invalidate();
        }
    }
}
