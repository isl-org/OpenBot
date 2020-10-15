# 贡献

<p align="center">
  <a href="README.md">English</a> |
  <span>简体中文</span>
</p>

## 流程

1. 提交一个问题，描述你要实现的改变。如果只是小的改动/bug修复，你可以跳到第3步。
2. 在issue中讨论了范围之后，把它分配给自己。它应该显示在OpenBot项目的 "To DO"一栏中。
3. Fork 项目并克隆到本地。
    
   `git clone https://github.com/<user_id>/OpenBot.git`。

4. 创建一个分支，并将其命名为`<user_id>/<feature>`，其中`<feature>`简明扼要地描述工作范围。

   `git checkout -b <user_id>/<feature>`。
   
5. 做好工作，写好提交信息，把你的分支推送到分叉的仓库里。
   
   ```
   git add <修改后的文件>
   git commit -m <有意义的描述>。
   git push --set-upstream origin <user_id>/<feature>。
   ```
   
6. 在GitHub上创建一个[pull request](https://github.com/intel-isl/OpenBot/pulls)，并将问题链接到它。它应该会显示在OpenBot项目的 "In progress "栏中。
7. 处理你可能收到的任何代码审查反馈，并将其推送到你的fork中。pull request会自动更新。
8. 来一杯你喜欢的冷饮，奖励你让世界变得更美好。

## 指南

- 使用与其他代码相同的样式和格式。
  - 对于Android代码，你可以运行以下命令。
    1. `./gradlew checkStyle`-->返回样式不正确的Java文件。
    2. `./gradlew applyStyle`-->将必要的样式更改应用于所有Java文件。
  - 对于Arduino和Python代码，只需尝试混合即可。
- 更新与你所做的代码更改相关的文档。
- 如果你想加入第三方的依赖关系，请先在问题中讨论。
- pull request应该尽可能少地实现单一功能的改动。
- 确保你不包含临时文件或二进制文件 (gitignores 应该主要处理这个问题)。
- 在提交拉取请求之前，先将master分支重写/合并到你的分支中。
- 如果可能的话，在Windows、Linux和OSX上测试你的代码。


如果你正在寻找更多关于为开源项目做贡献的信息，这里有两个很好的参考资料。

- [如何为开源做贡献](http://opensource.guide/how-to-contribute/)
- [为GitHub项目做贡献的初学者指南(英文)](https://akrabat.com/the-beginners-guide-to-contributing-to-a-github-project/)

非常感谢你！