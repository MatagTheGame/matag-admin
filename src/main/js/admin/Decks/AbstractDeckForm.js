import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import ApiClient from 'admin/utils/ApiClient'
import {bindActionCreators} from 'redux'
import DecksSelectorUtils from 'admin/Decks/DecksSelectorUtils'

class AbstractDeckForm extends Component {
  constructor(props) {
    super(props)
    this.handlePlay = this.handlePlay.bind(this)
  }

  handlePlay(event) {
    event.preventDefault()
    const request = {
      gameType: 'UNLIMITED',
      playerOptions: JSON.stringify({
        type: DecksSelectorUtils.getDeckType(this.props.state),
        options: this.props.playerOptions
      })
    }

    this.props.setSelectLoading()
    ApiClient.post('/game', request)
      .then(r => {
        this.props.setSelectLoaded()
        if (r.gameId > 0) {
          this.props.setStartLoading()
          this.props.goToGame(r.gameId)

        } else {
          if (r.error) {
            this.props.setSetError(r.error)
          }
          if (r.activeGameId) {
            this.props.setSetActiveGameId(r.activeGameId)
          }
        }
      })
  }

  displayGoToGame() {
    if (this.props.activeGameId) {
      return <span>Go to <a href='#' onClick={() => this.props.goToGame(this.props.activeGameId)}>game #{this.props.activeGameId}</a></span>
    }
  }

  displayError() {
    if (this.props.error) {
      return (
        <p className='message'>
          <span className='error'>{this.props.error}</span>
          {this.displayGoToGame()}
        </p>
      )
    }
  }

  displayLoader() {
    if (this.props.selectLoading || this.props.startLoading) {
      return <Loader center/>
    }
  }

  render() {
    return (
      <form id='play-form' className='matag-form' onSubmit={this.handlePlay}>
        <input type='hidden' name='session' value={AuthHelper.getToken()} />
        { this.props.children }
        { this.displayError() }
        { this.displayLoader() }
        <div className='grid three-columns'>
          <div/>
          <input type='submit' id='play-button' value='Play'/>
        </div>
      </form>
    )
  }
}

const deckSelectLoading = () => {
  return {
    type: 'DECK_SELECT_LOADING',
  }
}

const deckSelectLoaded = () => {
  return {
    type: 'DECK_SELECT_LOADED',
  }
}

const deckStartLoading = () => {
  return {
    type: 'DECK_START_LOADING',
  }
}

const deckSetError = (error) => {
  return {
    type: 'DECK_SET_ERROR',
    value: error
  }
}

const deckSetActiveGameId = (gameId) => {
  return {
    type: 'DECK_SET_ACTIVE_GAME_ID',
    value: gameId
  }
}

const mapStateToProps = state => {
  return {
    state: state,
    matagGameUrl: get(state, 'config.matagGameUrl', ''),
    selectLoading: get(state, 'decks.select.loading', false),
    startLoading: get(state, 'decks.start.loading', false),
    error: get(state, 'decks.start.error', ''),
    activeGameId: get(state, 'decks.start.activeGameId', ''),
    playerOptions: get(state, 'decks.' + DecksSelectorUtils.getDeckType(state))
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    setSelectLoading: bindActionCreators(deckSelectLoading, dispatch),
    setSelectLoaded: bindActionCreators(deckSelectLoaded, dispatch),
    setStartLoading: bindActionCreators(deckStartLoading, dispatch),
    setSetError: bindActionCreators(deckSetError, dispatch),
    setSetActiveGameId: bindActionCreators(deckSetActiveGameId, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(AbstractDeckForm)