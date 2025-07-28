import { z } from 'zod';

const USERNAME_MESSAGE = 'Vui lòng nhập tên nhân viên';
const PASSWORD_MESSAGE = 'Vui lòng nhập mật khẩu';
const EMAIL_MESSAGE = 'Vui lòng nhập email đúng định dạng';
const OLD_PASSWORD_MESSAGE = 'Vui lòng nhập mật khẩu cũ';
const NEW_PASSWORD_MESSAGE = 'Vui lòng nhập mật khẩu mới';
const PASSWORDS_NOT_MATCH_MESSAGE = 'Mật khẩu không khớp';

export const loginSchema = z.object({
  username: z.string({ message: USERNAME_MESSAGE }),
  password: z.string({ message: PASSWORD_MESSAGE }),
});

export const resetPasswordRequestSchema = z.object({
  email: z.string().email({ message: EMAIL_MESSAGE }),
});

export const resetPasswordSchema = z.object({
  oldPassword: z.string({ message: OLD_PASSWORD_MESSAGE }),
  newPassword: z.string({ message: NEW_PASSWORD_MESSAGE }),
});

export const registerEmailSchema = z.object({
  email: z.string().email({ message: EMAIL_MESSAGE }),
});

export const registerSchema = z
  .object({
    username: z.string({ message: USERNAME_MESSAGE }),
    password: z.string({ message: PASSWORD_MESSAGE }),
    email: z.string().email({ message: EMAIL_MESSAGE }),
    confirmPassword: z.string(),
  })
  .refine(data => data.confirmPassword === data.password, {
    message: PASSWORDS_NOT_MATCH_MESSAGE,
    path: ['confirmPassword'],
  });

export type LoginSchema = z.infer<typeof loginSchema>;
export type ResetPasswordRequestSchema = z.infer<typeof resetPasswordRequestSchema>;
export type ResetPasswordSchema = z.infer<typeof resetPasswordSchema>;
export type RegisterEmailSchema = z.infer<typeof registerEmailSchema>;
export type RegisterSchema = z.infer<typeof registerSchema>;
