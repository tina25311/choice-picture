import { createI18n } from 'vue-i18n'

const messages = {
  'zh-CN': {
    welcome: {
      title: '欢迎参与穿搭喜好调查',
      subtitle: '为了直播能被更多的人喜欢，希望大家来选择是否喜欢图片上的穿搭',
      description: '您的意见对我们非常重要，请积极参与调查！',
      startButton: '开始调查'
    },
    survey: {
      progress: '进度',
      of: '共',
      images: '张图片',
      like: '喜欢',
      dislike: '不喜欢',
      next: '下一张',
      submit: '提交调查'
    },
    completion: {
      title: '恭喜！',
      subtitle: '您已成功完成调查问卷',
      message: '感谢您的参与，您的反馈将帮助我们提供更好的内容！',
      backToHome: '返回首页',
      viewStats: '查看统计结果'
    },
    stats: {
      title: '调查统计结果',
      subtitle: '查看所有参与者的调查数据统计',
      totalResponses: '总参与人数',
      likesChart: '图片喜欢统计',
      dislikesChart: '图片不喜欢统计',
      deviceTypeChart: '终端类型统计',
      imagePreview: '图片预览',
      backToHome: '返回首页'
    },
    common: {
      loading: '加载中...',
      error: '发生错误',
      success: '操作成功'
    }
  },
  'en-US': {
    welcome: {
      title: 'Welcome to Fashion Preference Survey',
      subtitle: 'To make our livestream more popular, we need your help to choose whether you like the outfits in the images',
      description: 'Your opinion is very important to us, please participate actively!',
      startButton: 'Start Survey'
    },
    survey: {
      progress: 'Progress',
      of: 'of',
      images: 'images',
      like: 'Like',
      dislike: 'Dislike',
      next: 'Next',
      submit: 'Submit Survey'
    },
    completion: {
      title: 'Congratulations!',
      subtitle: 'You have successfully completed the survey',
      message: 'Thank you for your participation, your feedback will help us provide better content!',
      backToHome: 'Back to Home',
      viewStats: 'View Statistics'
    },
    stats: {
      title: 'Survey Statistics',
      subtitle: 'View statistics from all survey participants',
      totalResponses: 'Total Responses',
      likesChart: 'Image Likes Statistics',
      dislikesChart: 'Image Dislikes Statistics',
      deviceTypeChart: 'Device Type Statistics',
      imagePreview: 'Image Preview',
      backToHome: 'Back to Home'
    },
    common: {
      loading: 'Loading...',
      error: 'An error occurred',
      success: 'Operation successful'
    }
  }
}

// 检测浏览器语言
function detectLanguage() {
  const browserLang = navigator.language || navigator.userLanguage
  if (browserLang.startsWith('zh')) {
    return 'zh-CN'
  }
  return 'en-US'
}

const i18n = createI18n({
  legacy: true, // 启用legacy模式以支持Options API
  locale: detectLanguage(),
  fallbackLocale: 'en-US',
  messages,
  globalInjection: true, // 确保$t函数在模板中可用
  allowComposition: true // 同时支持Composition API
})

export default i18n 