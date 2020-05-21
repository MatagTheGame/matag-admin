import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import ApiClient from 'admin/utils/ApiClient'
import ActiveGame from './ActiveGame'
import PlayForm from './PlayForm'
import './play.scss'

class Play extends Component {
  componentDidMount() {
    this.props.loadActiveGame()
    ApiClient.get('/game').then(this.props.activeGameLoaded)
    this.goToGame = this.goToGame.bind(this)
  }

  displayMain() {
    if (this.props.loading) {
      return <Loader center/>

    } else if (this.props.activeGame.gameId) {
      return <ActiveGame activeGame={this.props.activeGame} goToGame={this.goToGame} />

    } else {
      return <PlayForm goToGame={this.goToGame} />
    }
  }

  goToGame(id) {
    ApiClient.postToUrl(this.props.matagGameUrl + '/ui/game/' + id, {session: AuthHelper.getToken()})
  }

  render() {
    return (
      <section id='play'>
        <h2>Play</h2>
        { this.displayMain() }
      </section>
    )
  }
}

const loadActiveGame = () => {
  return {
    type: 'ACTIVE_GAME_LOADING'
  }
}

const activeGameLoaded = (activeGame) => {
  return {
    type: 'ACTIVE_GAME_LOADED',
    value: activeGame
  }
}

const mapStateToProps = state => {
  return {
    loading: get(state, 'play.activeGame.loading', true),
    activeGame: get(state, 'play.activeGame.value', {}),
    matagGameUrl: get(state, 'config.matagGameUrl', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loadActiveGame: bindActionCreators(loadActiveGame, dispatch),
    activeGameLoaded: bindActionCreators(activeGameLoaded, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Play)