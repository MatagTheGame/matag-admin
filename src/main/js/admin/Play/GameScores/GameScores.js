import React, {Component} from 'react'
import {connect} from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import Loader from 'admin/Common/Loader'
import LoggedInFunctionalityErrorMessage from 'admin/Common/LoggedInFunctionalityErrorMessage'
import AuthHelper from 'admin/Auth/AuthHelper'

class ScoreRow extends Component {
  render() {
    return (
      <tr key={this.props.score.id}>
        <td>{this.props.score.rank}</td>
        <td>{this.props.score.player}</td>
        <td>{this.props.score.elo}</td>
        <td>{this.props.score.wins}</td>
        <td>{this.props.score.draws}</td>
        <td>{this.props.score.losses}</td>
      </tr>
    )
  }
}

class GameScores extends Component {
  componentDidMount() {
    this.props.loadGameScores()
    ApiClient.get('/game/scores').then(this.props.gameScoresLoaded)
  }

  renderGameScores() {
    return (
      <table id='score-board-table'>
        <thead>
          <tr>
            <th>Rank</th>
            <th>Player</th>
            <th>ELO</th>
            <th>Wins</th>
            <th>Draws</th>
            <th>Losses</th>
          </tr>
        </thead>
        <tbody>
          {this.props.scores.map((score) =>
            <ScoreRow key={score.id} score={score} />
          )}
        </tbody>
      </table>
    )
  }

  renderMain() {
    if (this.props.loadingGameScores) {
      return <Loader fullscreen/>
    } else {
      return this.renderGameScores()
    }
  }

  render() {
    return (
      <section id='game-scores'>
        <h2>Game Scores</h2>
        { this.props.isLoggedIn ? this.renderMain() : <LoggedInFunctionalityErrorMessage/> }
      </section>
    )
  }
}

const loadGameScores = () => {
  return {
    type: 'GAME_SCORES_LOADING'
  }
}

const gameScoresLoaded = (gameScores) => {
  return {
    type: 'GAME_SCORES_LOADED',
    value: gameScores
  }
}

const mapStateToProps = state => {
  return {
    isLoggedIn: AuthHelper.isLoggedIn(state),
    loadingGameScores: get(state, 'play.gameScores.loading', true),
    scores: get(state, 'play.gameScores.value.scores', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadGameScores: bindActionCreators(loadGameScores, dispatch),
    gameScoresLoaded: bindActionCreators(gameScoresLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameScores)