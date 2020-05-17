import React from 'react'
import Provider from 'react-redux/lib/components/Provider'
import {createStore} from 'redux'
import fetchMock from 'fetch-mock'
import AdminApp from 'admin/AdminApp'
import history from 'admin/utils/history'
import {render} from '@testing-library/react'
import AppReducer from 'admin/_reducers/AppReducer'
import AuthHelper from 'admin/Auth/AuthHelper'

export default class TestUtils {
  static renderAdminApp (url = '/ui/admin') {
    const app = render(
      <Provider store={createStore(AppReducer)}>
        <AdminApp/>
      </Provider>
    )
    history.push(url)
    return app
  }

  static userIsLoggedIn() {
    TestUtils.mockProfile()
    TestUtils.mockAuthHelper()
  }

  static guestUser() {
    TestUtils.mockGuestProfile()
  }

  static mockConfig () {
    fetchMock.get('/config', {
      'matagName': 'Matag: The Game',
      'matagAdminUrl': 'http://localhost:8082',
      'matagGameUrl': 'http://localhost:8080',
      'matagSupportEmail': 'matag.the.game@gmail.com'
    })
  }

  static mockStats () {
    fetchMock.get('/stats', {
      'totalUsers': 2,
      'onlineUsers': 1,
      'totalCards': 300,
      'totalSets': 10
    })
  }

  static mockConfigAndStats() {
    TestUtils.mockConfig()
    TestUtils.mockStats()
  }

  static mockProfile () {
    fetchMock.get('/profile', {
      'username': 'User1',
      'type': 'USER'
    })
  }

  static mockGuestProfile () {
    fetchMock.get('/profile', {})
  }

  static mockActiveGame () {
    fetchMock.get('/game', {})
  }

  static mockAuthHelper() {
    AuthHelper.getToken = () => 'token-001'
  }

  static resetMocks() {
    fetchMock.reset()
  }
}
