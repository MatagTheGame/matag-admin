import React from 'react'
import {useDispatch, useSelector} from 'react-redux'
import AbstractDeckForm from 'admin/Decks/AbstractDeckForm'
import DecksSelectorUtils from 'admin/Decks/DecksSelectorUtils'
import PreConstructedDeckForm from 'admin/Decks/pre-constructed/PreConstructedDeckForm'
import CustomDeckForm from 'admin/Decks/custom/CustomForm'
import RandomDeckForm from 'admin/Decks/random/RandomDeckForm'

export default function({ goToGame }) {
  const dispatch = useDispatch()

  const deckType = useSelector(state => DecksSelectorUtils.getDeckType(state))

  const changeDeckType = (type) => dispatch({
    type: 'DECK_TYPE',
    value: type
  })

  const getTabHeaderClassName = (headerName) =>
    `tab-header ${deckType === headerName ? 'selected' : ''}`

  return (
    <section className='tab-container'>
      <div className='tab-list'>
        <div className={getTabHeaderClassName('random')} onClick={() => changeDeckType('random')}>
          <h3>Random</h3>
        </div>
        <div className={getTabHeaderClassName('pre-constructed')} onClick={() => changeDeckType('pre-constructed')}>
          <h3>PreConstructed</h3>
        </div>
        <div className={getTabHeaderClassName('custom')} onClick={() => changeDeckType('custom')}>
          <h3>Custom</h3>
        </div>
      </div>
      <div className='tab-content'>
        {deckType === 'random' && (
          <AbstractDeckForm goToGame={goToGame}><RandomDeckForm /></AbstractDeckForm>
        )}
        {deckType === 'pre-constructed' && (
          <AbstractDeckForm goToGame={goToGame}><PreConstructedDeckForm /></AbstractDeckForm>
        )}
        {deckType === 'custom' && (
          <AbstractDeckForm goToGame={goToGame}><CustomDeckForm /></AbstractDeckForm>
        )}
      </div>
    </section>
  )
}
