import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import Loader from 'admin/Common/Loader'
import LoggedInFunctionalityErrorMessage from 'admin/Common/LoggedInFunctionalityErrorMessage'
import AuthHelper from 'admin/Auth/AuthHelper'

function ScoreRow({ score, username }) {
  const isRowSelected = score.player === username ? 'player-selected' : ''

  return (
    <tr>
      <td className={isRowSelected}>{score.rank}</td>
      <td className={isRowSelected}>{score.player}</td>
      <td className={isRowSelected}>{score.elo}</td>
      <td className={isRowSelected}>{score.wins}</td>
      <td className={isRowSelected}>{score.draws}</td>
      <td className={isRowSelected}>{score.losses}</td>
    </tr>
  )
}

export default function GameScores() {
  const dispatch = useDispatch()

  const { isLoggedIn, loading, scores, username } = useSelector(state => ({
    isLoggedIn: AuthHelper.isLoggedIn(state),
    loading: state.play?.gameScores?.loading ?? true,
    scores: state.play?.gameScores?.value?.scores || [],
    username: state.session?.profile?.username || ''
  }))

  useEffect(() => {
    if (isLoggedIn) {
      dispatch({ type: 'GAME_SCORES_LOADING' })
      ApiClient.get('/game/scores').then(response => {
        dispatch({ type: 'GAME_SCORES_LOADED', value: response })
      })
    }
  }, [dispatch, isLoggedIn])

  if (!isLoggedIn) {
    return (
      <section id='game-scores'>
        <h2>Game Scores</h2>
        <LoggedInFunctionalityErrorMessage />
      </section>
    )
  }

  return (
    <section id='game-scores'>
      <h2>Game Scores</h2>
      {loading ? (
        <Loader fullscreen />
      ) : (
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
            {scores.map(score => (
              <ScoreRow key={score.id} score={score} username={username} />
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}