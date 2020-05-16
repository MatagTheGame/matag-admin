import React, {Component} from 'react'
import {connect} from 'react-redux'
import NonGuestFunctionalityErrorMessage from "admin/Common/NonGuestFunctionalityErrorMessage";
import AuthHelper from "admin/Auth/AuthHelper";
import ApiClient from "admin/utils/ApiClient";
import get from "lodash/get";
import {bindActionCreators} from "redux";

class GameHistory extends Component {
  componentDidMount() {
    this.props.loadGameHistory()
    ApiClient.get('/game/history').then(this.props.gameHistoryLoaded)
  }

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

const loadGameHistory = () => {
  return {
    type: 'GAME_HISTORY_LOADING'
  }
}

const gameHistoryLoaded = (gameHistory) => {
  return {
    type: 'GAME_HISTORY_LOADED',
    value: gameHistory
  }
}

const mapStateToProps = state => {
  return {
    isNonGuest: AuthHelper.isNonGuest(state),
    loadingGameHistory: get(state, 'play.gameHistory.loading', false),
    gameHistory: get(state, 'play.gameHistory', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadGameHistory: bindActionCreators(loadGameHistory, dispatch),
    gameHistoryLoaded: bindActionCreators(gameHistoryLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameHistory)