import React, {Component} from 'react'
import {connect} from 'react-redux'

class ScoreBoard extends Component {
  render() {
    return (
      <section id='score-board'>
        <h2>ScoreBoard</h2>
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

export default connect(mapStateToProps, mapDispatchToProps)(ScoreBoard)