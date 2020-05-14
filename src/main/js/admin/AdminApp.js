import React, {Component} from 'react'
import {Route, Router, Switch} from 'react-router-dom'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Login from 'admin/Auth/Login/Login'
import Register from 'admin/Auth/Register/Register'
import Verify from 'admin/Auth/Verify/Verify'
import Loader from 'admin/Common/Loader'
import Decks from 'admin/Decks/Decks'
import Header from 'admin/Header/Header'
import Home from 'admin/Home/Home'
import ApiClient from 'admin/utils/ApiClient'
import history from 'admin/utils/history'
import Play from './Play/Play'
import GameHistory from "admin/Play/GameHistory/GameHistory";
import ScoreBoard from "admin/Play/ScoreBoard/ScoreBoard";
import Profile from "admin/Profile/Profile";
import ProfileUtils from 'admin/Profile/ProfileUtils'
import './admin.scss'

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
          <Router history={history}>
            <div>
              <Header/>
              <Switch>
                <Route path="/ui/admin" exact>
                  <div className='page with-margin'><Home/></div>
                </Route>

                <Route path="/ui/admin/auth/login">
                  <div className='page with-margin'><Login/></div>
                </Route>
                <Route path="/ui/admin/auth/register">
                  <div className='page with-margin'><Register/></div>
                </Route>
                <Route path="/ui/admin/auth/verify">
                  <div className='page with-margin'><Verify/></div>
                </Route>

                <Route path="/ui/admin/decks">
                  <div className='page'><Decks/></div>
                </Route>

                <Route path="/ui/admin/play">
                  <div className='page with-margin'><Play/></div>
                </Route>
                <Route path="/ui/admin/game-history">
                  <div className='page with-margin'><GameHistory/></div>
                </Route>
                <Route path="/ui/admin/score-board">
                  <div className='page with-margin'><ScoreBoard/></div>
                </Route>

                <Route path="/ui/admin/profile">
                  <div className='page with-margin'><Profile/></div>
                </Route>
              </Switch>
            </div>
          </Router>
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
