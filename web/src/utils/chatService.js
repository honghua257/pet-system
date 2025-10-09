import request from '@/utils/http'

/**
 * AI聊天相关接口
 */
export class ChatService {
  /**
   * AI聊天接口
   * @param msg 用户消息
   * @returns Promise<AI回复内容>
   */
  static chat(msg) {
    return request.get("/ai/chat", {
      params: { msg }
    })
  }

  /**
   * 流式聊天接口 - 实时返回AI回复
   * @param msg 用户消息
   * @param onChunk 处理每个数据块的回调函数
   * @returns Promise<void>
   */
  static async chatStream(msg, onChunk) {
    try {
      const response = await fetch(`${import.meta.env.VITE_APP_API_URL}/ai/chat/str?msg=${encodeURIComponent(msg)}`, {
        method: 'GET',
        headers: {
          'token': localStorage.getItem('token') || '',
          'Content-Type': 'text/plain'
        }
      })

      if (!response.ok) {
        throw new Error('Network response was not ok')
      }

      const reader = response.body?.getReader()
      const decoder = new TextDecoder()

      if (reader) {
        while (true) {
          const { done, value } = await reader.read()
          if (done) break

          const chunk = decoder.decode(value)
          onChunk(chunk)
        }
      }
    } catch (error) {
      console.error('Stream chat error:', error)
      onChunk('抱歉，连接出现错误，请重试。')
    }
  }

  /**
   * 获取ChatResponse格式流式响应
   * @param msg 用户消息
   * @param onChunk 处理每个数据块的回调函数
   * @returns Promise<void>
   */
  static async chatResponseStream(msg, onChunk) {
    try {
      const response = await fetch(`${import.meta.env.VITE_APP_API_URL}/ai/chat/response?msg=${encodeURIComponent(msg)}`, {
        method: 'GET',
        headers: {
          'token': localStorage.getItem('token') || '',
          'Content-Type': 'application/json'
        }
      })

      if (!response.ok) {
        throw new Error('Network response was not ok')
      }

      const reader = response.body?.getReader()
      const decoder = new TextDecoder()

      if (reader) {
        while (true) {
          const { done, value } = await reader.read()
          if (done) break

          const chunk = decoder.decode(value)
          try {
            const data = JSON.parse(chunk)
            onChunk(data)
          } catch (e) {
            onChunk(chunk)
          }
        }
      }
    } catch (error) {
      console.error('Response stream error:', error)
      onChunk({ error: '连接出现错误，请重试。' })
    }
  }
}