package com.example.democomm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnOnOff, btnDiscover, btnSend, btnConnect;
    ListView listView,lv2;
    TextView read_msg_box, connectionStatus;
    EditText writeMsg;
    public OutputStream outputStream;
    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;



    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray, deviceArrays;
    WifiP2pDevice device;
    static final int MESSAGE_READ=1;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv2 = (ListView) findViewById(R.id.peerListView2);
        listView = (ListView) findViewById(R.id.peerListView);
        initialWork();
        myListener();
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
                case MESSAGE_READ:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    read_msg_box.setText(tempMsg);
                    break;
            }
            return true;
        }
    });

    //New code

    public void registerService (int port) {

        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        serviceInfo.setServiceName("ANB");
        serviceInfo.setServiceType("_anb._tcp");
        serviceInfo.setPort(port);


    }

    public void initializeServerSocket () throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        int localPort = serverSocket.getLocalPort();

    }


    private void startRegistration() {

        Map record = new HashMap();
        record.put("listenport", String.valueOf(8888));
        record.put("buddy","Traveller"+(int)(Math.random()*1000));
        record.put("available","visible");

        Log.d("Backend","inside startRegistration");
        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance("_testing","_presence._tcp",record);
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("Backend","inside startRegistration Success");
            }

            @Override
            public void onFailure(int i) {
                Log.d("Backend","inside startRegistration Failure");
            }
        });
    }
    final HashMap<String, String> buddies = new HashMap<String, String>();
    private void discoverService() {
        Log.d("Backend","inside discoverService");
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {

            /* Callback includes:
             * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
             * record: TXT record dta as a map of key/value pairs.
             * device: The device running the advertised service.
             */

            @Override
            public void onDnsSdTxtRecordAvailable(String s, Map record, WifiP2pDevice device) {
                buddies.put(device.deviceAddress, (String) record.get("buddy"));


            }

        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice resourceType) {
                resourceType.deviceName = buddies.containsKey(resourceType.deviceAddress) ? buddies
                        .get(resourceType.deviceAddress) : resourceType.deviceName;

                // show adapter
               // deviceArrays=new WifiP2pDevice[buddies.values().size()];
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
                adapter.add(String.valueOf(resourceType.deviceName));
                adapter.notifyDataSetChanged();
                lv2.setAdapter(adapter);





                //writeMsg.setText(resourceType.toString());
                Log.d("Ok advertise","I am"+instanceName);
            }
        };
        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);
        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("Backend","inside discoverService Success");
            }

            @Override
            public void onFailure(int i) {
                Log.d("Backend","inside discoverService Failure");
            }
        });

        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
            private static final String TAG = "Error";

            @Override
            public void onSuccess() {
                Log.d("Backend","Success!");
                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                //added on 03/04
               // Intent i = getIntent();
                //String name = i.getStringExtra("NofD");
                //Log.d("Device Selected",name);
                //String msg=writeMsg.getText().toString();
                //sendReceive.write(msg.getBytes());
            }

            @Override
            public void onFailure(int i) {
                if (i == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d(TAG, "P2P isn't supported on this device.");
                }
            }

        });
    }


//till here new code



    private void myListener() {
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wifiManager.isWifiEnabled())
                {
                    wifiManager.setWifiEnabled(false);
                    btnOnOff.setText("ON");
                }else {
                    wifiManager.setWifiEnabled(true);
                    btnOnOff.setText("OFF");
                }
            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectionStatus.setText("Discovery Started");
                    }

                    @Override
                    public void onFailure(int i) {
                        connectionStatus.setText("Discovery Starting Failed");
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device=deviceArray[i];
                WifiP2pConfig config=new WifiP2pConfig();
                config.deviceAddress=device.deviceAddress;

               // Intent intent = new Intent(getApplicationContext(),MainActivity.class);
               // intent.putExtra("NofD",deviceArray[i]);

               // i++;
              // mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {

               mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Connected to "+device.deviceName,Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device=deviceArray[i];
                WifiP2pConfig config=new WifiP2pConfig();
                config.deviceAddress=device.deviceAddress;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Connected: Group Formed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(),"Connected to "+device.deviceName,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Backend","Message "+writeMsg.getText().toString());
                String msg=writeMsg.getText().toString();
              //  String msg="HEllO";
                sendReceive.write(msg.getBytes());
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startRegistration();
                discoverService();
            }
        });

       /* btnConnect.setOnClickListener(new View.OnClickListener() {
            //int i=0;
            @Override
            public void onClick(View view) {
                for (int k = 0; k < peers.size(); k++) {
                    final WifiP2pDevice device = peers.get(k);
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    config.wps.setup = WpsInfo.PBC;
                    mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Log.d("Backend","Inside connect success");
                           // String msg = "Hello to All";
                         //   sendReceive.write(msg.getBytes());
                            WifiP2pManager.PeerListListener list =null;
                            list.onPeersAvailable((WifiP2pDeviceList) peers);
                            mManager.requestPeers(mChannel,list);
                           *//* mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Backend","Inside removeGroup success");
                                    Toast.makeText(MainActivity.this,"Connecting to another peer",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                @Override
                                public void onFailure(int i) {
                                    Log.d("Backend","Failed disconnecting from peers");
                                    return;
                                }
                            });*//*
                        }

                        @Override
                        public void onFailure(int i) {
                            Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });*/
    }


    private void initialWork() {
        btnOnOff=(Button) findViewById(R.id.onOff);
        btnDiscover=(Button) findViewById(R.id.discover);
        btnSend=(Button) findViewById(R.id.sendButton);
        btnConnect = (Button)findViewById(R.id.connect);
        listView=(ListView) findViewById(R.id.peerListView);
        read_msg_box=(TextView) findViewById(R.id.readMsg);
        connectionStatus=(TextView) findViewById(R.id.connectionStatus);
        writeMsg=(EditText) findViewById(R.id.writeMsg);

        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);

        mReceiver=new WiFiDirectBroadcastReceiver(mManager, mChannel,this);

        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers))
            {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray=new String[peerList.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peerList.getDeviceList().size()];
                int index=0;

                for(WifiP2pDevice device : peerList.getDeviceList())
                {
                    deviceNameArray[index]=device.deviceName;
                    deviceArray[index]=device;
                    index++;
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                listView.setAdapter(adapter);
            }

            if(peers.size()==0)
            {
                Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;

            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
                connectionStatus.setText("Host");
                serverClass=new ServerClass();
                Log.d("Backend","Server Class Start");
                serverClass.start();
            }else if(wifiP2pInfo.groupFormed)
            {
                connectionStatus.setText("Client");
                clientClass=new ClientClass(groupOwnerAddress);
                Log.d("Backend","Client Class Start");
                clientClass.start();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {

                Log.d("Backend","Server run Start");
                serverSocket=new ServerSocket(8888);
                socket=serverSocket.accept();
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;


        public SendReceive(Socket skt)
        {  Log.d("Backend","Inside SendReceive constructor Class");
            socket=skt;
            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Log.d("Backend","Inside SendReceive run Class");
            byte[] buffer=new byte[1024];
            int bytes;

            while (socket!=null)
            {
                try {
                    bytes=inputStream.read(buffer);
                    if(bytes>0)
                    {
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        { Log.d("Backend","Inside SendReceive write Class");
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Log.d("Backend","Inside Write");
                outputStream.write(bytes);
                outputStream.flush();
               // socket.close();
              //  new SendMessage().execute();
            } catch (Exception e) {
                Log.d("Backend","Inside ERROR of write");
                e.printStackTrace();
            }
        }
    }

    public class ClientClass extends Thread{
        Socket socket;
        String hostAdd;

        public  ClientClass(InetAddress hostAddress)
        {
            hostAdd=hostAddress.getHostAddress();
            socket=new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   /* public class SendMessage extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object... objects) {
            String a="asasa";
            try {
                outputStream.write(a.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/
}
