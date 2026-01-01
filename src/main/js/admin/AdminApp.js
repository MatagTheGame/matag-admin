import React, {Component} from 'react'
import {Route, BrowserRouter, Routes} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import get from 'lodash/get'
import ChangePassword from 'admin/Auth/ChangePassword/ChangePassword'
import Login from 'admin/Auth/Login/Login'
import Register from 'admin/Auth/Register/Register'
import Verify from 'admin/Auth/Verify/Verify'
import Loader from 'admin/Common/Loader'
import Decks from 'admin/Decks/Decks'
import Header from 'admin/Header/Header'
import Home from 'admin/Home/Home'
import ApiClient from 'admin/utils/ApiClient'
import Play from './Play/Play'
import GameHistory from 'admin/Play/GameHistory/GameHistory'
import GameScores from 'admin/Play/GameScores/GameScores'
import Profile from 'admin/Profile/Profile'
import ProfileUtils from 'admin/Profile/ProfileUtils'
import './admin.scss'

export const APP_BASE_PATH = '/matag/admin'

class AdminApp extends Component {
  componentDidMount() {
    ApiClient.get('/config').then(this.props.configLoaded)
    ProfileUtils.getProfile().then(this.props.profileLoaded)
  }

  render() {
    if (this.props.loading) {
      return <Loader fullscreen/>

    } else {
      return (
        <BrowserRouter basename={APP_BASE_PATH}>
          <div>
            <Header/>
            <Routes>
              <Route path="/ui" element={<div className='page with-margin'><Home/></div>} />

              <Route path="/ui/auth/login" element={<div className='page with-margin'><Login/></div>} />
              <Route path="/ui/auth/register" element={<div className='page with-margin'><Register/></div>} />
              <Route path="/ui/auth/verify" element={<div className='page with-margin'><Verify/></div>} />
              <Route path="/ui/auth/change-password" element={<div className='page with-margin'><ChangePassword/></div>} />

              <Route path="/ui/decks" element={<div className='page'><Decks/></div>} />

              <Route path="/ui/play" element={<div className='page with-margin'><Play/></div>} />
              <Route path="/ui/play/game-history" element={<div className='page with-margin'><GameHistory/></div>} />
              <Route path="/ui/play/score-board" element={<div className='page with-margin'><GameScores/></div>} />

              <Route path="/ui/profile" element={<div className='page with-margin'><Profile/></div>} />

              {/* 4. Fallback route uses path="*" */}
              <Route path="*" element={<div className='page with-margin'><Home/></div>} />
            </Routes>
          </div>
        </BrowserRouter>
      )
    }
  }
}

const profileLoaded = (profile) => {
  return {
    type: 'PROFILE_LOADED',
    value: profile
  }
}

const configLoaded = (config) => {
  return {
    type: 'CONFIG_LOADED',
    value: config
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'session.loading', true),
  }
}

const mapDispatchToProps = dispatch => {
  return {
    configLoaded: bindActionCreators(configLoaded, dispatch),
    profileLoaded: bindActionCreators(profileLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(AdminApp)
