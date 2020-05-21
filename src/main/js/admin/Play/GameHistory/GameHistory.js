import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import ApiClient from 'admin/utils/ApiClient'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import NonGuestFunctionalityErrorMessage from 'admin/Common/NonGuestFunctionalityErrorMessage'
import DateUtils from 'admin/utils/DateUtils'

class GameHistoryRow extends Component {
  render() {
    return (
      <tr key={this.props.gameHistory.gameId}>
        <td>{DateUtils.formatDateTime(DateUtils.parse(this.props.gameHistory.startedTime))}</td>
        <td>{this.props.gameHistory.player1Name}</td>
        <td>{this.props.gameHistory.player2Name}</td>
        <td>{this.props.gameHistory.result}</td>
        <td>{this.props.gameHistory.type}</td>
      </tr>
    )
  }
}

class GameHistory extends Component {
  componentDidMount() {
    if (this.props.isNonGuest) {
      this.props.loadGameHistory()
      ApiClient.get('/game/history').then(this.props.gameHistoryLoaded)
    }
  }

  renderGameHistory() {
    return (
      <table id='game-history-table'>
        <thead>
          <tr>
            <th>Time</th>
            <th>Player1</th>
            <th>Player2</th>
            <th>Result</th>
            <th>Game Type</th>
          </tr>
        </thead>
        <tbody>
          {this.props.gamesHistory.map((gameHistory) =>
            <GameHistoryRow key={gameHistory.gameId} gameHistory={gameHistory} />
          )}
        </tbody>
      </table>
    )
  }

  renderMain() {
    if (this.props.loadingGameHistory) {
      return <Loader fullscreen/>
    } else {
      return this.renderGameHistory()
    }
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
    gamesHistory: get(state, 'play.gameHistory.value.gamesHistory', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadGameHistory: bindActionCreators(loadGameHistory, dispatch),
    gameHistoryLoaded: bindActionCreators(gameHistoryLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameHistory)