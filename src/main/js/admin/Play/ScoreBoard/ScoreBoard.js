import React, {Component} from 'react'
import {connect} from 'react-redux'

class ScoreBoard extends Component {
  render() {
    return (
      <section id='score-board'>
        <h2>Score Board</h2>
        Coming soon
      </section>
    )
  }
}

const mapStateToProps = () => {
  return {

  }
}

const mapDispatchToProps = () => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(ScoreBoard)