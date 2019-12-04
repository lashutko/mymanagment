<?php

namespace Dotenv\Regex;

use PhpOption\Option;

class Regex
{
    /**
     * Perform a preg replace, wrapping up the result.
     *
     * @param string $pattern
     * @param string $replacement
     * @param string $task
     *
     * @return \Dotenv\Regex\Result
     */
    public static function replace($pattern, $replacement, $task)
    {
        return self::pregAndWrap(function ($task) use ($pattern, $replacement) {
            return preg_replace($pattern, $replacement, $task);
        }, $task);
    }

    /**
     * Perform a preg replace callback, wrapping up the result.
     *
     * @param string   $pattern
     * @param callable $callback
     * @param string   $task
     *
     * @return \Dotenv\Regex\Result
     */
    public static function replaceCallback($pattern, callable $callback, $task)
    {
        return self::pregAndWrap(function ($task) use ($pattern, $callback) {
            return preg_replace_callback($pattern, $callback, $task);
        }, $task);
    }

    /**
     * Perform a preg operation, wrapping up the result.
     *
     * @param callable $operation
     * @param string   $task
     *
     * @return \Dotenv\Regex\Result
     */
    private static function pregAndWrap(callable $operation, $task)
    {
        $result = (string) @$operation($task);

        if (($e = preg_last_error()) !== PREG_NO_ERROR) {
            return Error::create(self::lookupError($e));
        }

        return Success::create($result);
    }

    /**
     * Lookup the preg error code.
     *
     * @param int $code
     *
     * @return string
     */
    private static function lookupError($code)
    {
        return Option::fromValue(get_defined_constants(true))
            ->filter(function (array $consts) {
                return isset($consts['pcre']) && defined('ARRAY_FILTER_USE_KEY');
            })
            ->map(function (array $consts) {
                return array_filter($consts['pcre'], function ($msg) {
                    return substr($msg, -6) === '_ERROR';
                }, ARRAY_FILTER_USE_KEY);
            })
            ->flatMap(function (array $errors) use ($code) {
                return Option::fromValue(
                    array_search($code, $errors, true)
                );
            })
            ->getOrElse('PREG_ERROR');
    }
}
