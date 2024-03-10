Page({
  // 初始数据
  data: {
    showUploadTip: false,
    uploading: false,
    downloadUrl: '',
    envId: 'kuaidi-env-1-6gs70q01358f0a3d'
  },

  onLoad() {
  },

  // 选择excel
  chooseExcel() {
    let that = this
    wx.chooseMessageFile({
      count: 1,
      type: 'file',
      extension: ["xlsx"],
      success(res) {
        let path = res.tempFiles[0].path
        console.log("选择excel成功", path)
        that.uploadExcel(path);
      }
    })
  },

  // 上传excel到云存储
  uploadExcel(path) {
    let that = this
    wx.showLoading({
      title: "正在上传",
    });
    wx.cloud.uploadFile({
      cloudPath: new Date().getTime() + '.xlsx',
      filePath: path,
      config: {
        env: this.data.envId
      },
      success: res => {
        let fileID = res.fileID
        console.log("上传成功", fileID)
        wx.hideLoading()
        wx.showLoading({
          title: "正在解析",
        });
        that.parseExcel(fileID)
      },
      fail: err => {
        console.log("上传失败", err)
        wx.hideLoading();
        wx.showLoading({
          title: '上传失败',
        });
      }
    })
  },

  //把数据保存到excel里，并把excel保存到云存储
  parseExcel(fileID) {
    let that = this
    wx.cloud.callFunction({
      name: 'quickstartFunctions',
      config: {
        env: this.data.envId
      },
      data: {
        type: 'parseExcel',
        data: fileID
      },
      success(res) {
        let fileID = res.result.fileID
        console.log("解析成功", fileID)
        wx.hideLoading();
        wx.showLoading({
          title: '解析成功',
        });
        that.getDownloadUrl(fileID)
      },
      fail(err) {
        console.log("解析失败", err)
        wx.hideLoading();
        wx.showLoading({
          title: '解析失败',
        });
      }
    })
  },

  //获取云存储文件下载地址，这个地址有效期一天
  getDownloadUrl(fileID) {
    let that = this;
    wx.cloud.getTempFileURL({
      fileList: [fileID],
      success: res => {
        that.setData({
          downloadUrl: res.fileList[0].tempFileURL
        })
        console.log("获取云存储文件下载链接成功", res.fileList[0].tempFileURL)
        wx.hideLoading();
        wx.showLoading({
          title: '获取链接成功',
        });
        wx.hideLoading();
      },
      fail: err => {
        console.log("换取云存储文件下载链接失败", err)
        wx.hideLoading();
        wx.showLoading({
          title: '换取链接失败',
        });
      }
    })
  },

  //复制excel文件下载链接
  copyDownloadUrl() {
    let that=this
    wx.setClipboardData({
      data: that.data.downloadUrl,
      success: res => {
        wx.showToast({
          title: '去浏览器下载',
        });
      }
    })
  }
});