import FormError from 'app/components/form/form-error'
import React from 'react'
import { FormGroup, Label } from 'reactstrap'

type Props = {
  label?: string | React.ReactNode,
  children: React.ReactNode
  error?: string
  row?: boolean
}

const FormWrap = (props: Props) => {
  const { label, children, error, row } = props

  return (
    <FormGroup className='form-group-v2' row={row}>
      {label && <Label className='form-label-v2'>{label}</Label>}
      {children}
      {!!error && <FormError message={error} />}
    </FormGroup>
  )
}

export default FormWrap
