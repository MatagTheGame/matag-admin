import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import AuthHelper from 'admin/Auth/AuthHelper'
import Login from 'admin/Auth/Login/Login'
import Play from 'admin/Play/Play'
import Stats from './Stats/Stats'
import Intro from './Intro/Intro'
import './home.scss'

class Home extends Component {
  displayMainAction() {
    if (this.props.isLoggedIn) {
      return <Play/>
    } else {
      return <Login />
    }
  }

  render() {
    return (
      <section id='home'>
        <h2>Home</h2>
        <Intro config={this.props.config}/>
        <Stats/>
        { this.displayMainAction() }
      </section>
    )
  }
}

const mapStateToProps = state => {
  return {
    isLoggedIn: AuthHelper.isLoggedIn(state),
    config: get(state, 'config', {})
  }
}

const mapDispatchToProps = () => {
  return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(Home)