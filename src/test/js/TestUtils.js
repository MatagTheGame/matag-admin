import React from 'react'
import Provider from 'react-redux/lib/components/Provider'
import {createStore} from 'redux'
import fetchMock from 'fetch-mock'
import {mount} from 'enzyme'
import AdminApp from 'admin/AdminApp'
import history from 'admin/utils/history'
import AppReducer from 'admin/_reducers/AppReducer'
import AuthHelper from 'admin/Auth/AuthHelper'

export default class TestUtils {
  static mountAdminApp(url = '/ui/admin') {
    const app = mount(
      <Provider store={createStore(AppReducer)}>
        <AdminApp/>
      </Provider>
    )
    history.push(url)
    return app
  }

  static mockConfig () {
    fetchMock.get('/config', {
      'matagName': 'Matag: The Game',
      'matagAdminUrl': 'http://localhost:8082',
      'matagGameUrl': 'http://localhost:8080',
      'matagSupportEmail': 'matag.the.game@gmail.com'
    })
  }

  static mockProfile () {
    fetchMock.get('/profile', {
      'username': 'User1',
      'type': 'USER'
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

  static mockActiveGame () {
    fetchMock.get('/game', {})
  }

  static mockAuthHelper() {
    AuthHelper.getToken = () => 'token-001'
  }
}
