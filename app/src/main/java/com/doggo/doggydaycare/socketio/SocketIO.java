package com.doggo.doggydaycare.socketio;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

// Singleton containing the SocketIO connection.
public class SocketIO extends Application
{
    private Socket mSocket;
    {
        try
        {
            mSocket = IO.socket("http://54.193.106.184/DoggoDB/doggoAPI.php");
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //this method is called from various Android components that want to use SocketIO
    public Socket getSocket()
    {
        return mSocket;
    }
}

