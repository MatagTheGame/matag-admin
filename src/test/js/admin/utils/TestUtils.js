import React from 'react'
import { Provider } from 'react-redux'
import {createStore} from 'redux'
import {getNodeText, render} from '@testing-library/react'
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
    history.push('/matag/admin/ui' + url)
    return app
  }

  static loginUser() {
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

  static tableDataAsStrings(table) {
    const trs = table.querySelectorAll('tbody tr')
    return Array.from(trs).map((tr) => TestUtils.rowAsStrings(tr))
  }

  static rowAsStrings(row) {
    const tds = row.querySelectorAll('td')
    return Array.from(tds)
      .map((td) => getNodeText(td))
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
  'onlineUsers': ['Player1', 'Player2'],
  'totalCards': 300,
  'totalSets': 10
}

TestUtils.DEFAULT_PROFILE = {
  'username': 'User1',
  'type': 'USER'
}