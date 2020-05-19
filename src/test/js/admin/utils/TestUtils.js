import React from 'react'
import Provider from 'react-redux/lib/components/Provider'
import {createStore} from 'redux'
import {render} from '@testing-library/react'
import AdminApp from 'admin/AdminApp'
import history from 'admin/utils/history'
import AppReducer from 'admin/_reducers/AppReducer'
import AuthHelper from 'admin/Auth/AuthHelper'
import ApiClientStub from './ApiClientStub'

export default class TestUtils {
  static renderAdminApp (url = '') {
    const app = render(
      <Provider store={createStore(AppReducer)}>
        <AdminApp/>
      </Provider>
    )
    history.push('/ui/admin' + url)
    return app
  }

  static userIsLoggedIn() {
    ApiClientStub.stubProfile()
    TestUtils.mockAuthHelper()
  }

  static userIsNotLoggedIn() {
    ApiClientStub.stubProfile({})
  }

  static defaultConfigAndStats() {
    ApiClientStub.stubConfig()
    ApiClientStub.stubStats()
  }

  static mockAuthHelper() {
    AuthHelper.getToken = () => 'token-001'
  }
}

TestUtils.DEFAULT_CONFIG = {
  'matagName': 'Matag: The Game',
  'matagAdminUrl': 'http://localhost:8082',
  'matagGameUrl': 'http://localhost:8080',
  'matagSupportEmail': 'matag.the.game@gmail.com'
}

TestUtils.DEFAULT_STATS = {
  'totalUsers': 2,
  'onlineUsers': 1,
  'totalCards': 300,
  'totalSets': 10
}

TestUtils.DEFAULT_PROFILE = {
  'username': 'User1',
  'type': 'USER'
}