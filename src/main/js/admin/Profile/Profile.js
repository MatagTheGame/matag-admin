import React, {Component} from 'react'
import {connect} from 'react-redux'

class Profile extends Component {
  render() {
    return (
      <section id='profile'>
        <h2>Profile</h2>
        Coming soon
      </section>
    )
  }
}

const mapStateToProps = state => {
  return {
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Profile)