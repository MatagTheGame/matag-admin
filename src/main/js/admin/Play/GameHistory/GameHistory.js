import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import NonGuestFunctionalityErrorMessage from 'admin/Common/NonGuestFunctionalityErrorMessage'
import DateUtils from 'admin/utils/DateUtils'

function GameHistoryRow({ gameHistory, username }) {
  const isSelected = (player) => player === username ? 'player-selected' : ''

  return (
    <tr>
      <td>{DateUtils.formatDateTime(DateUtils.parse(gameHistory.startedTime))}</td>
      <td className={isSelected(gameHistory.player1Name)}>{gameHistory.player1Name}</td>
      <td className={isSelected(gameHistory.player2Name)}>{gameHistory.player2Name}</td>
      <td>{gameHistory.result}</td>
      <td>{gameHistory.type}</td>
    </tr>
  )
}

export default function GameHistory() {
  const dispatch = useDispatch()

  const { isNonGuest, loading, gamesHistory, username } = useSelector(state => ({
    isNonGuest: AuthHelper.isNonGuest(state),
    loading: state.play?.gameHistory?.loading ?? true,
    gamesHistory: state.play?.gameHistory?.value?.gamesHistory || [],
    username: state.session?.profile?.username || ''
  }))

  useEffect(() => {
    if (isNonGuest) {
      dispatch({ type: 'GAME_HISTORY_LOADING' })
      ApiClient.get('/game/history').then(response => {
        dispatch({ type: 'GAME_HISTORY_LOADED', value: response })
      })
    }
  }, [dispatch, isNonGuest])

  if (!isNonGuest) {
    return (
      <section id='game-history'>
        <h2>Game History</h2>
        <NonGuestFunctionalityErrorMessage />
      </section>
    )
  }

  return (
    <section id='game-history'>
      <h2>Game History</h2>
      {loading ? (
        <Loader fullscreen />
      ) : (
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
            {gamesHistory.map(game => (
              <GameHistoryRow
                key={game.gameId}
                gameHistory={game}
                username={username}
              />
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}