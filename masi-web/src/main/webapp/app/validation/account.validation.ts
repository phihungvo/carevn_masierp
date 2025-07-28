import { z } from 'zod';

export const accountSchema = z.object({
  lastName: z.string({ message: 'Vui lòng nhập họ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập họ' }),
  firstName: z.string({ message: 'Vui lòng nhập tên' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên' }),
  email: z.string({ message: 'Vui lòng nhập email' }).email({ message: 'Email không hợp lệ' }),
  signatureId: z.string({ message: 'Vui lòng chọn chữ ký' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn chữ ký' }),
});

export type AccountSchema = z.infer<typeof accountSchema>;

export const udpateAccountSchema = z
  .object({
    password: z.string({ message: 'Vui lòng nhập mật khẩu' }).min(4, { message: 'Mật khẩu phải có ít nhất 6 ký tự' }),
    username: z.string({ message: 'Vui lòng nhập tên đăng nhập' }).max(50, { message: 'Tên đăng nhập không được quá 50 ký tự' }),
    repeatPassword: z.string({ message: 'Vui lòng nhập lại mật khẩu' }),
  })
  .refine(data => data.password === data.repeatPassword, { message: 'Mật khẩu không khớp', path: ['repeatPassword'] });

export type UpdateAccountSchema = z.infer<typeof udpateAccountSchema>;

export const createAccountSchema = z
.object({
  username: z.string({ message: 'Vui lòng nhập tên đăng nhập' }).max(50, { message: 'Tên đăng nhập không được quá 50 ký tự' }),
})

export type CreateAccountSchema = z.infer<typeof createAccountSchema>;

export const setCompanySchema = z.object({
  company: z.array(
    z.object({
      id: z.string().optional(),
      isChecked: z.boolean().optional(),
    }).optional()
  )
  .refine(value => {
    return value?.some((item) => item?.isChecked);
  }, { message: 'Vui lòng chọn ít nhất 1 công ty' })
  .transform(value => value.filter((item) => {
    if (item.isChecked) {
      return item.id;
    }
  }))
})
export type SetCompanySchema = z.infer<typeof setCompanySchema>;
