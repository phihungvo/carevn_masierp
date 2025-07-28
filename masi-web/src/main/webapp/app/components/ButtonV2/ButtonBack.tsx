import { useNavigate } from "react-router"
import ButtonV2 from "./ButtonV2"
import { ComponentProps } from "react"

type Props = ComponentProps<typeof ButtonV2> & {
  children?: string | React.ReactNode
}

const ButtonBack = (props: Props) => {
  const { children } = props

  const navigate = useNavigate()
  const onCancel = () => navigate(-1)

  return (
    <ButtonV2 onClick={onCancel}>{children}</ButtonV2>
  )
}

ButtonBack.defaultProps = {
  children: 'Đóng'
}

export default ButtonBack
