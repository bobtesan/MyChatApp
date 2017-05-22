package com.example.raul.mychatapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;



public class Client extends AsyncTask<Void,Void,String> {
    private String dstAdress;
    private int dstPort;
    private String msg;
    private  OutputStream outStream;
    private EditText et;

    public Client(String adress,int port,String msg){
        dstAdress=adress;
        dstPort=port;
        this.msg=msg;
    }
    @Override
    protected String doInBackground(Void... arg0){
        Socket socket=null;
        String received="";
        try{
            socket=new Socket(dstAdress,dstPort);
            outStream= socket.getOutputStream();
            outStream.write(msg.getBytes());
            //publishProgress();


            byte[] buffer=new byte[1024];
            int bytesRead;
            InputStream inputStream=socket.getInputStream(); //citeste de pe socket
            while((bytesRead=inputStream.read(buffer))!=-1){
                String str=new String(buffer,0,bytesRead,"UTF-8");
                Log.d("TAG",str);
                received +=str;
            }

        }catch(UnknownHostException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(socket!=null){
                try{
                    socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return received;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("TAG", "Received: " + result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        et.setEnabled(true);

    }

   public void sendMsg(String msg){
        try{
            outStream.write(msg.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
