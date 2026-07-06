import axios from 'axios'

export const http = axios.create({
  baseURL: '/api',
  timeout: 30000,
})

interface ApiErrorResponse {
  code: string
  message: string
  fieldErrors?: Array<{
    field: string
    message: string
  }>
}

export function getApiErrorMessage(error: unknown) {
  if (!axios.isAxiosError<ApiErrorResponse>(error)) {
    return 'Something went wrong.'
  }

  const response = error.response?.data
  const firstFieldError = response?.fieldErrors?.[0]

  if (firstFieldError?.message) {
    return firstFieldError.message
  }

  if (response?.message) {
    return response.message
  }

  return 'Request failed. Please try again.'
}
