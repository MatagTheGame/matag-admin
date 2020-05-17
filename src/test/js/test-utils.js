import React from "react"
import {combineReducers, createStore} from "redux";
import AppReducer from "admin/_reducers/AppReducer";
import {connectRouter} from "connected-react-router";
import history from "admin/utils/history";
import {render} from '@testing-library/react'
import Provider from "react-redux/lib/components/Provider";
import AdminApp from "admin/AdminApp";
import fetchMock from "fetch-mock";

export const rootReducer = combineReducers({
  reducer: AppReducer,
  router: connectRouter(history)
})

export const renderAdminApp = (url = '/ui/admin') => {
  const app = render(
    <Provider store={createStore(rootReducer)}>
      <AdminApp/>
    </Provider>
  )
  history.push(url)
  return app
}

export const mockConfig = () => {
  fetchMock.get('/config', {
    "matagName": "Matag: The Game",
    "matagAdminUrl": "http://localhost:8082",
    "matagGameUrl": "http://localhost:8080",
    "matagSupportEmail": "matag.the.game@gmail.com"
  })
}

export const mockProfile = () => {
  fetchMock.get('/profile', {
    "username": "User1",
    "type": "USER"
  })
}