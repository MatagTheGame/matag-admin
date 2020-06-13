import React from 'react'
import get from 'lodash/get'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import DateUtils from 'admin/utils/DateUtils'
import AbstractForm from 'admin/utils/AbstractForm'


class ActiveGame extends AbstractForm {
  constructor(props) {
    super(props)
    this.cancelGame = this.cancelGame.bind(this)
  }

  cancelGame() {
    this.props.deletingActiveGame()
    ApiClient.delete('/game/' + this.props.activeGame.gameId)
      .then(response => this.props.deletedActiveGame(response))
  }

  render() {
    const game = this.props.activeGame

    return (
      <div>
        <p>You have already a game in progress:</p>

        <div className='matag-card active-game'>
          <dl>
            <dt>Game id: </dt>
            <dd>{game.gameId}</dd>
            <dt>Created at: </dt>
            <dd>{DateUtils.formatDateTime(DateUtils.parse(game.createdAt))}</dd>
            <dt>Player name: </dt>
            <dd>{game.playerName}</dd>
            <dt>Player options: </dt>
            <dd>{game.playerOptions}</dd>
            <dt>Opponent name: </dt>
            <dd>{game.opponentName}</dd>
            <dt>Opponent options: </dt>
            <dd>{game.opponentOptions}</dd>
          </dl>

          { this.displayError() }

          <div className='matag-form'>
            <div className='grid grid-50-50'>
              <input type='button' value='Go to Game' onClick={() => this.props.goToGame(game.gameId)} />
              <input type='button' value='Cancel Game' onClick={this.cancelGame} />
            </div>
          </div>
        </div>

      </div>
    )
  }
}

const deletingActiveGame = () => {
  return {
    type: 'ACTIVE_GAME_DELETING'
  }
}

const deletedActiveGame = (response) => {
  return {
    type: 'ACTIVE_GAME_DELETED',
    response: response
  }
}

const mapStateToProps = state => {
  return {
    activeGame: get(state, 'play.activeGame.value', {}),
    deleting: get(state, 'play.activeGame.deleting', false),
    error: get(state, 'play.activeGame.error', null),
    matagGameUrl: get(state, 'config.matagGameUrl', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    deletingActiveGame: bindActionCreators(deletingActiveGame, dispatch),
    deletedActiveGame: bindActionCreators(deletedActiveGame, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(ActiveGame)