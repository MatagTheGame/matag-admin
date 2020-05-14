import React, {Component} from 'react'
import {connect} from 'react-redux'

class GameHistory extends Component {
  render() {
    return (
      <section id='game-history'>
        <h2>GameHistory</h2>
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

export default connect(mapStateToProps, mapDispatchToProps)(GameHistory)