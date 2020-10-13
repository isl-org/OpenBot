
# 驾驶策略(高级)

<p align="center">
  <a href="README.md">English</a> |
  <span>简体中文</span>
</p>

警告：要为您的自定义数据集制定良好的驾驶策略，需要一些耐心。 它不是直接的，涉及数据收集，超参数调整等。如果您以前从未训练过机器学习模型，这将是挑战，甚至可能令人沮丧。

为了训练自动驾驶策略，你首先需要收集一个数据集。你收集的数据越多，得出的驾驶策略就越好。对于我们论文中的实验，我们收集了大约30分钟的数据。注意，网络会模仿你的驾驶行为。你的驾驶行为越好、越稳定，网络就会越好地学习驾驶。

## 数据收集

1. 将蓝牙游戏遥控手柄连接到手机上（如PS4 遥控手柄）。
2. 在应用程序中选择AUTOPILOT_F网络。
3. 现在通过游戏遥控手柄驱动汽车并记录数据集。在PS4控制手柄上可以用 **X** 按钮切换记录。

你现在会在智能手机的内部存储上找到一个名为Openbot的文件夹。对于每个记录，都会有一个压缩文件。zip文件的名称格式为*yyyymmdd_hhmmss.zip*，对应记录开始的时间戳。

你的数据集文件应该以以下结构存储。

```markdown
dataset
└── train_data
 └── my_openbot_1
  └── recording_1
      recording_2
      ...
     my_openbot_2
     ...
 test_data
 └── my_openbot_3
  └── recording_1
      recording_2
      ...
```

从手机上的*Openbot*文件夹中导出，每一条记录都对应着一个解压的压缩文件。

## 训练策略

您首先需要设置您的培训环境。

### 依赖

我们建议为OpenBot创建一个conda环境。关于安装conda的说明可以在[这里](https://docs.conda.io/projects/conda/en/latest/user-guide/install/)找到。


如果你没有专用的GPU（例如使用你的笔记本电脑），你可以用以下命令创建一个新的环境。

```bash
conda create -n openbot python=3.7 tensorflow=2.0.0 notebook=6.1.1 matplotlib=3.3.1 pillow=7.2.0
```

请注意，训练速度会非常慢。所以，如果你具有专用GPU的计算机，我们强烈建议使用它。在这种情况下，你将需要支持GPU的Tensorflow。运行以下命令来设置conda环境。

```bash
conda create -n openbot python=3.7 tensorflow-gpu=2.0.0 notebook=6.1.1 matplotlib=3.3.1 pillow=7.2.0
```

如果你喜欢手动设置环境，这里有一个依赖列表。

- Tensorflow
- Jupyter Notebook
- Matplotlib
- Numpy
- PIL

### Jupyter Notebook

我们提供了一个 [Jupyter Notebook](policy_learning.ipynb)文件，指导您完成训练自动驾驶策略的步骤。笔记本会生成两个`tflite`文件，分别对应根据验证指标的最佳checkpoint和最新checkpoint。选取其中一个并将其重命名为autopilot_float.tflite。替换现有的模型，在

```markdown
app
└── assets
 └── networks
  └── autopilot_float.tflite
```

并重新编译安卓App。
