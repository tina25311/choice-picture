import axios from 'axios'

// const API_BASE_URL = 'http://192.168.1.102:8080/api'
// const API_BASE_URL = 'https://terry776-choice-picture.hf.space/api'
// const API_BASE_URL = 'https://luna776-choice-picture.hf.space/api'
const API_BASE_URL = 'https://backend.linuxapp.me/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    console.log('API Request:', config.method?.toUpperCase(), config.url)
    return config
  },
  error => {
    console.error('API Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    console.log('API Response:', response.status, response.data)
    return response
  },
  error => {
    console.error('API Response Error:', error.response?.data || error.message)
    return Promise.reject(error)
  }
)

export const surveyAPI = {
  // 获取图片ID列表
  getImageIds() {
    return api.get('/survey/images')
  },

  // 提交调查问卷
  submitSurvey(data) {
    return api.post('/survey/submit', data)
  },

  // 获取调查统计
  getStats() {
    return api.get('/survey/stats')
  },

  // 获取详细统计数据
  getDetailedStats() {
    return api.get('/survey/stats/detailed')
  }
}

export default api 