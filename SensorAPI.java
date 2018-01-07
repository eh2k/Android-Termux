package com.termux.api;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.JsonWriter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;

import com.termux.api.util.ResultReturner;

import java.util.List;

public class SensorAPI {

    static void onReceiveSensorsInfo(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {
            @Override
            public void writeJson(JsonWriter out) throws Exception {

                SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);

                if (sensors == null) {
		            out.beginObject().name("API_ERROR").value("Failed getting scan results").endObject();
                } else {
                    out.beginArray();
                    for (Sensor sensor : sensors) {
                        out.beginObject();
			            out.name("id").value(sensor.getId());
                        out.name("name").value(sensor.getName());
			            out.name("vendor").value(sensor.getVendor());
			            out.endObject();
			        }
          	        out.endArray();
                }
            }
        });
    }

    static void onReceiveSensorValues(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {

        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {

            @Override
            public void writeJson(JsonWriter out) throws Exception {

                Looper.prepare();
        
                class SensorActivity implements SensorEventListener {
                    public SensorManager mSensorManager;
                    public Sensor mSensor;
                    public float[] mValues;
                    final Looper _looper;
                  
                    public SensorActivity(int sensorType, Context context) {
        
                        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                        mSensor = mSensorManager.getDefaultSensor(sensorType);
                        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
                        _looper = Looper.myLooper();

                    }
                  
                    @Override
                    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
                      // Do something here if sensor accuracy changes.
                    }
                  
                    @Override
                    public final void onSensorChanged(SensorEvent event) {
        
                        mValues = event.values;
                        mSensorManager.unregisterListener(this);
                        _looper.quit();
                    }
                }
        
                final SensorActivity[] sensors = {
                    new SensorActivity(Sensor.TYPE_AMBIENT_TEMPERATURE, context),
                    new SensorActivity(Sensor.TYPE_RELATIVE_HUMIDITY, context)
                };
        
                Looper.loop();
                out.beginArray();
                for (SensorActivity s : sensors) {
                    out.beginObject();
                    out.name("id").value(s.mSensor.getId());
                    out.name("name").value(s.mSensor.getName());
                    out.name("vendor").value(s.mSensor.getVendor());
                    out.name("values").beginArray();
                    for (float value : s.mValues) {
                        //out.beginObject();
                        out.value(value);
                        //out.endObject();
                    }
                    out.endArray();
                    out.endObject();
                }
                out.endArray();
            }
        });
    }
}
