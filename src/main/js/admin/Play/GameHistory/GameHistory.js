import React, {Component} from 'react'
import {connect} from 'react-redux'
import NonGuestFunctionalityErrorMessage from "admin/Common/NonGuestFunctionalityErrorMessage";
import AuthHelper from "admin/Auth/AuthHelper";

class GameHistory extends Component {
  renderMain() {
    return (
      <>
        Coming soon
      </>
    )
  }

  render() {
    return (
      <section id='game-history'>
        <h2>Game History</h2>
        { this.props.isNonGuest ? this.renderMain() : <NonGuestFunctionalityErrorMessage/> }
      </section>
    )
  }
}

const mapStateToProps = state => {
  return {
    isNonGuest: AuthHelper.isNonGuest(state)
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameHistory)