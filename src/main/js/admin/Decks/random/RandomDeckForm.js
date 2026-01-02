import React, {useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {APP_BASE_PATH} from 'admin/AdminApp'
import ApiClient from 'admin/utils/ApiClient'
import { MultiSelect } from 'react-multi-select-component'

const ALL = '_ALL_'

export default function RandomDeckForm() {
  const dispatch = useDispatch()
  const dispatchDeckRandomEvent = (value) => dispatch({type: 'DECK_RANDOM', value})

  const allSets = useSelector(state => state.sets ?? [])
  const randomDeck = useSelector(state => state.decks.random ?? {colors: [], sets: [ALL]})

  useEffect(() => {
    ApiClient.get('/sets').then((sets) => dispatch({ type: 'SETS_LOADED', value: sets }))
  }, [dispatch])

  const isColorSelected = (color) => randomDeck.colors.includes(color)
  const isAllSetsSelected = () => randomDeck.sets.includes(ALL)
  const isSomeSetsSelected = () => !isAllSetsSelected()

  const toggleColor = (color) => {
    const colors = isColorSelected(color) ? randomDeck.colors.filter(it => it !== color) : [...randomDeck.colors, color]
    dispatchDeckRandomEvent({...randomDeck, colors})
  }

  const selectAllSets = () => dispatchDeckRandomEvent({ ...randomDeck, sets: [ALL] })
  const selectSomeSets = () => dispatchDeckRandomEvent({ ...randomDeck, sets: [] })
  const toggleSet = (sets) => {
    console.log('NioSets: ', sets)
    dispatchDeckRandomEvent({ ...randomDeck, sets: sets.map(({value}) => value) })
  }

  return (
    <>
      <p>What colors?</p>
      <ul>
        <ColorCheckbox color={'White'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Blue'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Black'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Red'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Green'} isSelected={isColorSelected} toggle={toggleColor} />
      </ul>

      <p>What set?</p>
      <ul>
        <SetRadio type={'All'} isSelected={isAllSetsSelected} toggle={selectAllSets} />
        <SetRadio type={'Some'} isSelected={isSomeSetsSelected} toggle={selectSomeSets} />
        { isSomeSetsSelected() && <SetDropDown allSets={allSets} selectedSets={randomDeck.sets} toggle={toggleSet} /> }
      </ul>
    </>
  )
}

const ColorCheckbox = ({ color, isSelected, toggle: toggle}) =>
  <li>
    <input type="checkbox" id={`color-${color}`} name={color} checked={isSelected(color.toUpperCase())} onChange={() => toggle(color.toUpperCase())}/>
    <label htmlFor={`color-${color}`}><img src={`${APP_BASE_PATH}/img/symbols/${color.toUpperCase()}.png`} alt={color}/>{color}</label>
  </li>

const SetRadio = ({ type, isSelected, toggle: toggle}) =>
  <li>
    <input id={`sets-${type}`} type="radio" value="SETS" name="sets" checked={isSelected()} onChange={toggle}/>
    <label htmlFor={`sets-${type}`}>{type}</label>
  </li>

const SetDropDown = ({allSets, selectedSets, toggle}) => {
  const options = allSets.map(({ name, code }) => ({
    label: `${name} (${code})`,
    value: code
  }))
  const selectedOptions = options.filter(opt => selectedSets.includes(opt.value))

  return (
    <li>
      <MultiSelect options={options} value={selectedOptions} onChange={toggle} labelledBy="Select"/>
    </li>
  )
}