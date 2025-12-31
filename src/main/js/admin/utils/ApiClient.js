import AuthHelper from 'admin/Auth/AuthHelper'

export const APP_BASE_PATH = '/matag/admin';

export default class ApiClient {

  static get(url) {
    return ApiClient.execute(url)
  }

  static getNoJson(url) {
    return ApiClient.executeNoJsonResponse(url)
  }

  static post(url, request) {
    return ApiClient.execute(url, request, 'POST')
  }

  static postNoJson(url, request) {
    return ApiClient.executeNoJsonResponse(url, request, 'POST')
  }

  static delete(url) {
    return ApiClient.execute(url, {}, 'DELETE')
  }

  static execute(url, request = undefined, method = 'GET') {
    return ApiClient.executeNoJsonResponse(url, request, method)
      .then(response => response.json())
      .catch(() => {return {'error': 'An error occurred while executing the request.'}})
  }

  static executeNoJsonResponse(url, request = undefined, method = 'GET') {
    return fetch(APP_BASE_PATH + url, {
      method: method,
      body: JSON.stringify(request),
      headers: {
        'Content-Type': 'application/json',
        'session': AuthHelper.getToken()
      }
    })
  }

  static postToUrl(path, params) {
    const form = document.createElement('form')
    form.setAttribute('method', 'POST')
    form.setAttribute('action', path)

    for (const key in params) {
      if (Object.prototype.hasOwnProperty.call(params, key)) {
        const hiddenField = document.createElement('input')
        hiddenField.setAttribute('type', 'hidden')
        hiddenField.setAttribute('name', key)
        hiddenField.setAttribute('value', params[key])

        form.appendChild(hiddenField)
      }
    }

    document.body.appendChild(form)
    form.submit()
  }
}