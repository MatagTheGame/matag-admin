import React from 'react'
import { useSelector, useDispatch } from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import DateUtils from 'admin/utils/DateUtils'
import FormError from 'admin/Form/FormError'

export default function ActiveGame({ goToGame }) {
  const dispatch = useDispatch()

  const { activeGame, error } = useSelector(state => ({
    activeGame: state.play?.activeGame?.value || {},
    error: state.play?.activeGame?.error || null
  }))

  const cancelGame = () => {
    dispatch({ type: 'ACTIVE_GAME_DELETING' })
    ApiClient.delete(`/game/${activeGame.gameId}`)
      .then(response => dispatch({ type: 'ACTIVE_GAME_DELETED', response }))
  }

  return (
    <div>
      <p>You have already a game in progress:</p>

      <div className='matag-card active-game'>
        <dl>
          <dt>Game id: </dt>
          <dd>{activeGame.gameId}</dd>
          <dt>Created at: </dt>
          <dd>{DateUtils.formatDateTime(DateUtils.parse(activeGame.createdAt))}</dd>
          <dt>Player name: </dt>
          <dd>{activeGame.playerName}</dd>
          <dt>Player options: </dt>
          <dd>{activeGame.playerOptions}</dd>
          <dt>Opponent name: </dt>
          <dd>{activeGame.opponentName}</dd>
          <dt>Opponent options: </dt>
          <dd>{activeGame.opponentOptions}</dd>
        </dl>

        <FormError error={error} />

        <div className='matag-form'>
          <div className='grid grid-50-50'>
            <input
              type='button'
              value='Go to Game'
              onClick={() => goToGame(activeGame.gameId)}
            />
            <input
              type='button'
              value='Cancel Game'
              onClick={cancelGame}
            />
          </div>
        </div>
      </div>
    </div>
  )
}