import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import ApiClient from 'admin/utils/ApiClient'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import ActiveGame from './ActiveGame'
import DecksSelector from '../Decks/DecksSelector'
import './play.scss'

export default function Play() {
  const dispatch = useDispatch()

  const { loading, activeGame, matagGameUrl } = useSelector(state => ({
    loading: state.play?.activeGame?.loading ?? true,
    activeGame: state.play?.activeGame?.value || {},
    matagGameUrl: state.config?.matagGameUrl || ''
  }))

  useEffect(() => {
    dispatch({ type: 'ACTIVE_GAME_LOADING' })
    ApiClient.get('/game').then(response => {
      dispatch({ type: 'ACTIVE_GAME_LOADED', value: response })
    })
  }, [dispatch])

  const goToGame = (id) => {
    ApiClient.postToUrl(`${matagGameUrl}/ui/${id}`, {
      session: AuthHelper.getToken()
    })
  }

  return (
    <section id='play'>
      <h2>Play</h2>
      {loading ? (
        <Loader center />
      ) : activeGame.gameId ? (
        <ActiveGame activeGame={activeGame} goToGame={goToGame} />
      ) : (
        <DecksSelector goToGame={goToGame} />
      )}
    </section>
  )
}