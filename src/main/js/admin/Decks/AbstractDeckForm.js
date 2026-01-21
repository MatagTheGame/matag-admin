import React from 'react'
import { useSelector, useDispatch } from 'react-redux'
import AuthHelper from 'admin/Auth/AuthHelper'
import Loader from 'admin/Common/Loader'
import ApiClient from 'admin/utils/ApiClient'
import DecksSelectorUtils from 'admin/Decks/DecksSelectorUtils'

export default function AbstractDeckForm({ children, goToGame }) {
  const dispatch = useDispatch()
  const state = useSelector(s => s)

  const deckType = DecksSelectorUtils.getDeckType(state)
  const { selectLoading, startLoading, error, activeGameId, playerOptions } = useSelector(s => ({
    selectLoading: s.decks?.select?.loading || false,
    startLoading: s.decks?.start?.loading || false,
    error: s.decks?.start?.error || '',
    activeGameId: s.decks?.start?.activeGameId || '',
    playerOptions: s.decks?.[deckType]
  }))

  const handlePlay = (event) => {
    event.preventDefault()
    const request = {
      gameType: 'UNLIMITED',
      playerOptions: JSON.stringify({
        type: deckType,
        options: playerOptions
      })
    }

    dispatch({ type: 'DECK_SELECT_LOADING' })
    ApiClient.post('/game', request).then(r => {
      dispatch({ type: 'DECK_SELECT_LOADED' })
      if (r.gameId > 0) {
        dispatch({ type: 'DECK_START_LOADING' })
        goToGame(r.gameId)
      } else {
        if (r.error) dispatch({ type: 'DECK_SET_ERROR', value: r.error })
        if (r.activeGameId) dispatch({ type: 'DECK_SET_ACTIVE_GAME_ID', value: r.activeGameId })
      }
    })
  }

  return (
    <form id='play-form' className='matag-form' onSubmit={handlePlay}>
      <input type='hidden' name='session' value={AuthHelper.getToken()} />
      {children}

      {error && (
        <p className='message'>
          <span className='error'>{error}</span>
          {activeGameId && (
            <span> Go to <a href='#' onClick={(e) => { e.preventDefault(); goToGame(activeGameId) }}>game #{activeGameId}</a></span>
          )}
        </p>
      )}

      {(selectLoading || startLoading) && <Loader center />}

      <div className='grid three-columns'>
        <div />
        <input type='submit' id='play-button' value='Play' />
      </div>
    </form>
  )
}