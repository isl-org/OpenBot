# Python server for log upload

## Dependencies

If you do not want install the dependencies globally, activate your conda environment first:

```
conda activate openbot
```

Make sure you are in the folder `policy`. Now, you can install all the dependencies with the following command:

```
pip install -r openbot/server/requirements.txt
```

## Running the server

You can run the python server with the command:

```
python -m openbot.server
```

There is also a developer mode:

```
adev runserver openbot/server
```

For frontend development (react app):

```
FE_DEV=1 adev runserver openbot/server
```

When you run the server you should see something like:

```
Skip address 127.0.0.1 @ interface lo0
Found address 192.168.x.x @ interface en0
Registration of a service, press Ctrl-C to exit...
======== Running on http://0.0.0.0:8000 ========
(Press CTRL+C to quit)
```

You can now open your browser to visualize the dataset and see incoming uploads by going to:

```
http://localhost:8000/uploaded/
```

<img src="../../../docs/images/server.gif" width="100%" alt="server" />

## Troubleshooting

If the upload to the server is not working, here are some troubleshooting tips:

- Try restarting the server (computer) and the OpenBot app (smartphone)
- Make sure the smartphone and your computer are connected to the same WiFi network
- If your router has both 2.4 GHz and 5 GHz networks with the same name, disable the 5 GHz network
- Keep the phone connected to Android Studio while running the app. In the Logcat tab, select Debug from the dropdown. Type `NSD` into the filter field to see the debug messages concerning the server connection. Type `Upload` into the filter field for debug messages concerning the recording file upload.
