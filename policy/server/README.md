# Python Server for Log Upload

## Dependencies

If you do not want install the dependencies globally, activate your conda environment first:

```
conda activate openbot
```

Make sure you are in the folder `policy/server`. Now, you can install all the dependencies with the following command:

```
pip install -r requirements.txt
```

You can run the python server with the command:

```
python main.py
```

There is also a developer mode:

```
adev runserver main.py
```

When you run the server you should see something like:

```
Dataset dir: <clone-dir>/OpenBot/policy/dataset
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

<img src="../../docs/images/server.gif" width="100%" alt="server" />




