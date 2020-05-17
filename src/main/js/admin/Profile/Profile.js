import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'

class Profile extends Component {
  render() {
    return (
      <section id='profile'>
        <h2>Profile</h2>
        <dl>
          <dt>Username: </dt>
          <dd>{this.props.profile.username}</dd>
          <dt>Type: </dt>
          <dd>{this.props.profile.type}</dd>
        </dl>
      </section>
    )
  }
}

const mapStateToProps = state => {
  return {
    profile: get(state, 'session.profile', {})
  }
}

const mapDispatchToProps = () => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Profile)